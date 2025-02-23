package com.example.ApiArenaXperience.dto.event;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record GetListEventoFilterDto(
        String name,
        @FutureOrPresent(message = "La fecha del evento no puede estar en el pasado.")
        LocalDate date,
        @Max(value = 800, message = "La capacidad no puede superar los 800.")
        int capacity
) {
}
