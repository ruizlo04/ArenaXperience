package com.example.ApiArenaXperience.dto.event;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditEventoCmd(
        @NotNull(message = "La fecha del evento no puede ser nula.")
        @FutureOrPresent(message = "La fecha del evento no puede estar en el pasado.")
        LocalDate date,
        @Min(value = 500, message = "La capacidad del aforo debe ser mínimo 500.")
        @Max(value = 800, message = "La capacidad no puede superar los 800.")
        int capacity
) {
}
