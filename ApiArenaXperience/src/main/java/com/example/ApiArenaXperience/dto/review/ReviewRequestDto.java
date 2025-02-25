package com.example.ApiArenaXperience.dto.review;

import jakarta.validation.constraints.*;

public record ReviewRequestDto(
        @NotBlank(message = "El nombre del evento no puede estar vacío")
        String eventoName,

        @NotNull(message = "La calificación no puede estar vacía")
        @Min(value = 0, message = "La calificación debe ser como mínimo 0")
        @Max(value = 10, message = "La calificación debe ser como máximo 10")
        int rating,

        @NotBlank(message = "El comentario no puede estar vacío")
        @Size(max = 150, message = "El comentario no puede tener más de 150 palabras")
        String comment
) {
}