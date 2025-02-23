package com.example.ApiArenaXperience.service.event;

import com.example.ApiArenaXperience.dto.event.CreateEventRequest;
import com.example.ApiArenaXperience.dto.event.EditEventoCmd;
import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.error.event.EventNotFoundException;
import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.repo.EventRepository;
import com.example.ApiArenaXperience.specification.EventoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public Evento createEvent(CreateEventRequest createEventRequest){

        Evento evento = Evento.builder()
                .name(createEventRequest.name())
                .date(createEventRequest.date())
                .capacity(createEventRequest.capacity())
                .build();

        return eventoRepository.save(evento);
    }

    public Evento editarEvento(String name, EditEventoCmd editEventoCmd) {
        Optional<Evento> optionalEvento = eventoRepository.findByName(name);
        if (optionalEvento.isEmpty()) {
            throw new RuntimeException("Evento no encontrado con nombre: " + name);
        }

        Evento evento = optionalEvento.get();
        evento.setDate(editEventoCmd.date());
        evento.setCapacity(editEventoCmd.capacity());

        return eventoRepository.save(evento);
    }

    @Transactional
    public void deleteEvent(String name) {
        Evento evento = eventoRepository.findByName(name)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con nombre: " + name));

        eventoRepository.delete(evento);
    }

}
