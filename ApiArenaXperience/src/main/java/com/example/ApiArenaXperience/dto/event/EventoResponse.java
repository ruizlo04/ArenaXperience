package com.example.ApiArenaXperience.dto.event;

import com.example.ApiArenaXperience.model.event.Evento;

import java.time.LocalDate;
import java.util.UUID;

public record EventoResponse(
        UUID id,
        String name,
        LocalDate date,
        int capacity
) {

    public static EventoResponse of (Evento event) {
        return new EventoResponse(
                event.getId(),
                event.getName(),
                event.getDate(),
                event.getCapacity()
        );
    }
}
