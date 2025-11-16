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

    // =============================
    // Public registration
    // =============================
    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("Email already exists"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER); // always USER

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(
                new AuthResponse("Registration successful", token, user.getUsername(), user.getRole())
        );
    }

    // =============================
    // Admin creates user with role
    // =============================
    public User createUserWithRole(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // set role or fallback to USER
        user.setRole(request.getRole() != null ? request.getRole() : Role.ROLE_USER);

        return userRepository.save(user);
    }

    // =============================
    // Delete user (Admin only)
    // =============================
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    // =============================
    // Login
    // =============================
    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtService.generateToken(user);
                return ResponseEntity.ok(
                        new AuthResponse("Login successful", token, user.getUsername(), user.getRole())
                );
            }
        }

        return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
    }
}
