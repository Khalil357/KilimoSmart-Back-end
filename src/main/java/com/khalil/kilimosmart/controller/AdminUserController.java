package com.khalil.kilimosmart.controller;

import com.khalil.kilimosmart.dto.RegisterRequest;
import com.khalil.kilimosmart.model.User;
import com.khalil.kilimosmart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // Create user with role (Admin only)
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
        User createdUser = userService.createUserWithRole(request);
        return ResponseEntity.ok("User created: " + createdUser.getEmail() + " with role " + createdUser.getRole());
    }

    // Delete user (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
