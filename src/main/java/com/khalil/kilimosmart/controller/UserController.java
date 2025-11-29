package com.khalil.kilimosmart.controller;

import com.khalil.kilimosmart.dto.AuthResponse;
import com.khalil.kilimosmart.dto.LoginRequest;
import com.khalil.kilimosmart.dto.RegisterRequest;
import com.khalil.kilimosmart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
