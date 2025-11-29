package com.khalil.kilimosmart.service;

import com.khalil.kilimosmart.dto.AuthResponse;
import com.khalil.kilimosmart.dto.LoginRequest;
import com.khalil.kilimosmart.dto.RegisterRequest;
import com.khalil.kilimosmart.model.Role;
import com.khalil.kilimosmart.model.User;
import com.khalil.kilimosmart.repository.UserRepository;
import com.khalil.kilimosmart.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtService jwtService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // Public registration (self-register as ROLE_USER)
    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse("Email already exists"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse("Registration successful", token, user.getUsername(), user.getRole()));
    }

    // Login
    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        System.out.println("LOGIN DEBUG: input email=[" + request.getEmail() + "], password=["
                + (request.getPassword() == null ? "null" : "PROVIDED") + "]");

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean match = passwordEncoder.matches(request.getPassword(), user.getPassword());
            System.out.println("LOGIN DEBUG: user found, password match=" + match);
            if (match) {
                String token = jwtService.generateToken(user);
                return ResponseEntity.ok(new AuthResponse("Login successful", token, user.getUsername(), user.getRole()));
            }
        }

        return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
    }

    // Admin creates user with a specific role
    public User createUserWithRole(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.ROLE_USER);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) throw new IllegalArgumentException("User not found");
        userRepository.deleteById(id);
    }
}
