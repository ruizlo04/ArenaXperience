package com.example.ApiArenaXperience.service.event;

import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.error.event.EventNotFoundException;
import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.repo.EventRepository;
import com.example.ApiArenaXperience.specification.EventoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventRepository eventoRepository;

    public List<EventoResponse> getAllEvents() {
        List<Evento> events = eventoRepository.findAll();

        if (events.isEmpty()) {
            throw new EventNotFoundException("No hay eventos creados.");
        }

        return events.stream()
                .map(EventoResponse::of)
                .collect(Collectors.toList());
    }

    public List<Evento> buscarEventos(String nombre, LocalDate fecha, Integer capacidad) {
        Specification<Evento> spec = EventoSpecification.filtrarEventos(nombre, fecha, capacidad);
        return eventoRepository.findAll(spec);
    }
}
