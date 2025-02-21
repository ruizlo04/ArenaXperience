package com.example.ApiArenaXperience.dto.user;

import com.example.ApiArenaXperience.validation.FieldsValueMatch;
import com.example.ApiArenaXperience.validation.UniqueUsername;
import jakarta.validation.constraints.NotBlank;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "verifyPassword",
                message = "Los valores de password y verifyPassword no coinciden")
})
public record CreateUserRequest(
        @NotBlank(message = "{createUserRequest.username.notblank}")
        @UniqueUsername(message = "El username ya existe")
        String username,
        String email,
        String password,
        String verifyPassword
) {
}
