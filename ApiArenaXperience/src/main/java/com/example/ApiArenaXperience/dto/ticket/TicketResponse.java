package com.example.ApiArenaXperience.dto.ticket;

import com.example.ApiArenaXperience.model.ticket.Ticket;

import java.util.UUID;

public record TicketResponse(
        UUID id,
        String eventName,
        double price
){
        public static TicketResponse of(Ticket ticket) {
            return new TicketResponse(
                    ticket.getId(),
                    ticket.getEvent().getName(),
                    ticket.getEvent().getPrice()
            );
        }
}
