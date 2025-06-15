package com.example.ApiArenaXperience.dto.ticket;

import com.example.ApiArenaXperience.model.ticket.Ticket;

import java.util.UUID;

public record TicketResponse(
        UUID id,
        String eventName,
        int cantidad,
        double precioFinal
) {
    public static TicketResponse of(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getEvent().getName(),
                ticket.getCantidad(),
                ticket.getPrecioFinal()
        );
    }
}

