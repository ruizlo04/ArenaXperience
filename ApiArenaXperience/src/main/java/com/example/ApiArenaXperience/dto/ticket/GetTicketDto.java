package com.example.ApiArenaXperience.dto.ticket;

import com.example.ApiArenaXperience.model.ticket.Ticket;

import java.util.UUID;

public record GetTicketDto(
        UUID id,
        UUID userId,
        String nombreEvento,
        double price
) {
    public static GetTicketDto of(Ticket ticket) {
        return new GetTicketDto(
                ticket.getId(),
                ticket.getUser().getId(),
                ticket.getEvent().getName(),
                ticket.getEvent().getPrice()
        );
    }
}
