package com.example.ApiArenaXperience.dto.ticket;

import com.example.ApiArenaXperience.model.ticket.Ticket;

import java.util.UUID;

public record TicketWithUserResponse(
        UUID id,
        String eventName,
        double price,
        String username,
        String userEmail
) {

    public static TicketWithUserResponse of(Ticket ticket) {
        return new TicketWithUserResponse(
                ticket.getId(),
                ticket.getEvent().getName(),
                ticket.getEvent().getPrice(),
                ticket.getUser().getUsername(),
                ticket.getUser().getEmail()
        );
    }
}
