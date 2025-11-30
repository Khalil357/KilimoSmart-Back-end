package com.khalil.kilimosmart.controller;

import com.khalil.kilimosmart.dto.RegisterRequest;
import com.khalil.kilimosmart.model.User;
import com.khalil.kilimosmart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // ADMIN: Create user with specific ROLE
    @PostMapping("/users/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
        User created = userService.createUserWithRole(request);
        return ResponseEntity.ok("Created user: " + created.getEmail() + " with role " + created.getRole());
    }

    // ADMIN: Delete user by ID
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }
}
