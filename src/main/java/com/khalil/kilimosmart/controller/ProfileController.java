package com.khalil.kilimosmart.controller;

import com.khalil.kilimosmart.dto.ChangePasswordRequest;
import com.khalil.kilimosmart.dto.UpdateProfileRequest;
import com.khalil.kilimosmart.model.User;
import com.khalil.kilimosmart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal User currentUser,
            @RequestBody UpdateProfileRequest request) {

        User updated = userService.updateProfile(currentUser, request);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal User currentUser,
            @RequestBody ChangePasswordRequest request) {

        userService.changePassword(currentUser, request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
