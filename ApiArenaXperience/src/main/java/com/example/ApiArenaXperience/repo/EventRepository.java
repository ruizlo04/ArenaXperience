package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.model.user.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Evento, UUID>, JpaSpecificationExecutor<Evento> {
    @Query("SELECT e FROM Evento e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Optional<Evento> findByName(String name);

    Page<Evento> findAll(Pageable pageable);
}
