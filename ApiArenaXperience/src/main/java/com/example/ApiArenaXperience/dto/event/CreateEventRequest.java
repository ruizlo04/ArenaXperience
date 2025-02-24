package com.example.ApiArenaXperience.dto.event;

import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.validation.ValidPrecio;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateEventRequest(
        @NotBlank(message = "El nombre del evento no puede estar vacío.")
        @Size(min = 7, message = "El nombre del evento debe tener al menos 7 caracteres.")
        @Pattern(
                regexp = "^(.{3,})-(.{3,})$",
                message = "El nombre del evento debe contener un guion (-) en el medio y al menos 3 caracteres antes y después."
        )
        String name,
        @NotNull(message = "La fecha del evento no puede ser nula.")
        @FutureOrPresent(message = "La fecha del evento no puede estar en el pasado.")
        LocalDate date,
        @Min(value = 500, message = "La capacidad del aforo debe ser mínimo 500.")
        @Max(value = 800, message = "La capacidad no puede superar los 800.")
        int capacity,
        @ValidPrecio
        double precio
) {
}
