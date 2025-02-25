package com.example.ApiArenaXperience.dto.review;

import com.example.ApiArenaXperience.model.review.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditResenyaCmd(
        @NotBlank(message = "La valoración no puede estar vacía")
        @Min(value = 0, message = "La calificación debe ser como mínimo 0")
        @Max(value = 10, message = "La calificación debe ser como máximo 10")
        int rating,
        @NotBlank(message = "El comentario no puede estar vacío")
        @Size(max = 150, message = "El comentario no puede tener más de 150 palabras")
        String comment
) {

    public static EditResenyaCmd of(Review review){
        return new EditResenyaCmd(
                review.getRating(),
                review.getComment()
        );
    }
}
