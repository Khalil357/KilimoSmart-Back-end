// src/main/java/com/khalil/kilimosmart/dto/AuthResponse.java
package com.khalil.kilimosmart.dto;

import com.khalil.kilimosmart.model.Role;

public class AuthResponse {
    private String message;
    private String token;
    private String username;
    private Role role;

    public AuthResponse() {}

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // ✅ Add this constructor to fix your error
    public AuthResponse(String message, String token, String username , Role role) {
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Role  getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
