package com.example.ApiArenaXperience.dto.user;

import com.example.ApiArenaXperience.validation.FieldsValueMatch;
import com.example.ApiArenaXperience.validation.UniqueUsername;
import com.example.ApiArenaXperience.validation.ValidPhoneNumber;
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
public record CreateUserRequest(
        @NotBlank(message = "Debes incluir un username")
        @UniqueUsername
        String username,
        @Email(message = "El email debe ser válido y contener un '@'")
        String email,
        String verifyEmail,
        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-@$!%*?&])[A-Za-z\\d-@$!%*?&]{8,}$",
                message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
        )
        String password,
        @NotBlank(message = "Debes confirmar la contraseña")
        String verifyPassword,
        @ValidPhoneNumber
        String phoneNumber
) {
}
