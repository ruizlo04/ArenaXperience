package com.example.ApiArenaXperience.dto.chat;

import jakarta.validation.constraints.NotBlank;

public record EditChatCmd(
        @NotBlank(message = "El mensaje no puede estar vacío")
        String message
) {
}
