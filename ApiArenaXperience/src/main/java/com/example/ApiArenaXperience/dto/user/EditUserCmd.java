package com.example.ApiArenaXperience.dto.user;

import com.example.ApiArenaXperience.validation.FieldsValueMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "verifyPassword",
                message = "Los valores de password y verifyPassword no coinciden"),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "verifyEmail",
                message = "Los valores de email y verifyEmail no coinciden")
})
public record EditUserCmd(
        @Email(message = "El email debe ser válido y contener un '@'")
        String email,
        String verifyEmail,
        String password,
        String verifyPassword,
        String phoneNumber
) {
}
