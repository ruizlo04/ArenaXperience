package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.event.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Evento, UUID> {
}
