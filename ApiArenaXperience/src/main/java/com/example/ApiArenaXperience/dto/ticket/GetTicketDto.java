package com.example.ApiArenaXperience.dto.ticket;

import com.example.ApiArenaXperience.model.ticket.Ticket;

import java.util.UUID;

public record GetTicketDto(
        UUID id,
        UUID userId,
        String nombreEvento,
        int cantidad,
        double precioFinal
) {
    public static GetTicketDto of(Ticket ticket) {
        return new GetTicketDto(
                ticket.getId(),
                ticket.getUser().getId(),
                ticket.getEvent().getName(),
                ticket.getCantidad(),
                ticket.getPrecioFinal()
        );
    }
}

