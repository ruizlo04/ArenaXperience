package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.event.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Evento, UUID>, JpaSpecificationExecutor<Evento> {
    Optional<Evento> findByName(String name);
}
