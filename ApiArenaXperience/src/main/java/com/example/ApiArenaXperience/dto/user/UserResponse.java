package com.example.ApiArenaXperience.dto.user;

import com.example.ApiArenaXperience.model.user.UserRole;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String phoneNumber,
        Set<UserRole> roles,
        Instant createdAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken
) {

    public static UserResponse of(Usuario user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRoles(),
                user.getCreatedAt(),
                null,
                null
        );
    }

    public static UserResponse of(Usuario user, String token, String refreshToken) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRoles(),
                user.getCreatedAt(),
                token,
                refreshToken
        );
    }
}
