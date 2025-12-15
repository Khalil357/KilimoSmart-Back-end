package com.khalil.kilimosmart.service;

import com.khalil.kilimosmart.dto.*;
import com.khalil.kilimosmart.model.Role;
import com.khalil.kilimosmart.model.User;
import com.khalil.kilimosmart.repository.UserRepository;
import com.khalil.kilimosmart.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

        // Normalize role
        Role role;
        if (request.getRole() == null || request.getRole().isBlank()) {
            role = Role.ROLE_USER; // default
        } else {
            try {
                // Convert "farmer" → "ROLE_FARMER"
                role = Role.valueOf("ROLE_" + request.getRole().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid role: " + request.getRole());
            }
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);  // ✅ assign the enum, not a string

        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) throw new IllegalArgumentException("User not found");
        userRepository.deleteById(id);
    }

    // Update profile
    public User updateProfile(User currentUser, UpdateProfileRequest request) {

        if (request.getEmail() != null && !request.getEmail().equals(currentUser.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already taken");
            }
            currentUser.setEmail(request.getEmail());
        }

        if (request.getUsername() != null) {
            currentUser.setUsername(request.getUsername());
        }

        return userRepository.save(currentUser);
    }

    // Change password
    public void changePassword(User currentUser, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



}
