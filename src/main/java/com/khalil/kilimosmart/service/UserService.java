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

    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new AuthResponse("Email already exists"));
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create user object
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        // Save user to DB
        userRepository.save(user);

        // Generate JWT after registration
        String token = jwtService.generateToken(user);

        // Return response with token, username, and role
        return ResponseEntity.ok(
                new AuthResponse("Registration successful", token, user.getUsername(), user.getRole())
        );
    }

    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Verify password
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // Generate JWT on successful login
                String token = jwtService.generateToken(user);
                return ResponseEntity.ok(
                        new AuthResponse("Login successful", token, user.getUsername(), user.getRole())
                );
            } else {
                return ResponseEntity
                        .status(401)
                        .body(new AuthResponse("Invalid credentials"));
            }
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new AuthResponse("Invalid credentials"));
        }
    }
}
