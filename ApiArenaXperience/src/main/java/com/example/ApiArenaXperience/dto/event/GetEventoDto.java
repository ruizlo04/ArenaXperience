package com.example.ApiArenaXperience.dto.event;

import com.example.ApiArenaXperience.model.event.Evento;

import java.time.LocalDate;
import java.util.UUID;

public record GetEventoDto(
        UUID id,
        String name,
        LocalDate date,
        int capacity
) {
    public static GetEventoDto of(Evento evento) {
        return new GetEventoDto(evento.getId(), evento.getName(), evento.getDate(), evento.getCapacity());
    }
}
