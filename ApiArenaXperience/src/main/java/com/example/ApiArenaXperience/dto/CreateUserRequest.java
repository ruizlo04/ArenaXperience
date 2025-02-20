package com.example.ApiArenaXperience.dto;

public record CreateUserRequest(
        String username, String email, String password, String verifyPassword) {
}
