package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
