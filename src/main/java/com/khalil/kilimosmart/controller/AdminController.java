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

    public AdminController(UserService userService) { this.userService = userService; }

    @PostMapping("/users/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
        User created = userService.createUserWithRole(request);
        return ResponseEntity.ok("Created: " + created.getEmail() + " as " + created.getRole());
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted");
    }
}
