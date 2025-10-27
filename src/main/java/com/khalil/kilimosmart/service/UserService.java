package com.khalil.kilimosmart.service;

import com.khalil.kilimosmart.dto.AuthResponse;
import com.khalil.kilimosmart.dto.LoginRequest;
import com.khalil.kilimosmart.dto.RegisterRequest;
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

    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse("Email already exists", null, null));
        }

        // Hash the password before saving it
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(request.getRole() != null ? request.getRole() : "USER");

        userRepository.save(user);

        // Generate JWT after registration
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse("Registration successful", token, user.getUsername()));
    }

    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Verify password securely
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // Generate JWT on successful login
                String token = jwtService.generateToken(user);
                return ResponseEntity.ok(new AuthResponse("Login successful", token, user.getUsername()));
            } else {
                return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials", null, null));
            }
        } else {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials", null, null));
        }
    }
}
