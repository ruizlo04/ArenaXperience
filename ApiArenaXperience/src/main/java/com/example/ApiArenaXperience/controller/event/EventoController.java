package com.example.ApiArenaXperience.controller.event;

import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.service.event.EventoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Endpoints para gestión de eventos")
public class EventoController {

    private final EventoService eventoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/events")
    public ResponseEntity<List<EventoResponse>> getAllEvents() {
        return ResponseEntity.ok(eventoService.getAllEvents());
    }
}
