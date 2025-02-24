package com.example.ApiArenaXperience.service.event;

import com.example.ApiArenaXperience.dto.event.CreateEventRequest;
import com.example.ApiArenaXperience.dto.event.EditEventoCmd;
import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.dto.ticket.TicketWithUserResponse;
import com.example.ApiArenaXperience.error.event.EventNotFoundException;
import com.example.ApiArenaXperience.error.user.UsersNotFoundException;
import com.example.ApiArenaXperience.files.model.FileMetadata;
import com.example.ApiArenaXperience.files.service.StorageService;
import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.model.ticket.Ticket;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.repo.EventRepository;
import com.example.ApiArenaXperience.repo.TicketRepository;
import com.example.ApiArenaXperience.repo.UserRepository;
import com.example.ApiArenaXperience.specification.EventoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventRepository eventoRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    public List<EventoResponse> getAllEvents() {
        List<Evento> events = eventoRepository.findAll();

        if (events.isEmpty()) {
            throw new EventNotFoundException("No hay eventos creados.");
        }

        return events.stream()
                .map(EventoResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TicketWithUserResponse> getTicketsForEvent(String eventName) {
        Evento evento = eventoRepository.findByName(eventName)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con nombre: " + eventName));

        Set<Ticket> tickets = evento.getTickets();

        return tickets.stream()
                .map(TicketWithUserResponse::of)
                .collect(Collectors.toList());
    }

    public List<Evento> buscarEventos(String nombre, LocalDate fecha, Integer capacidad) {
        Specification<Evento> spec = EventoSpecification.filtrarEventos(nombre, fecha, capacidad);
        return eventoRepository.findAll(spec);
    }

    public Evento createEvent(CreateEventRequest createEventRequest, MultipartFile file){

        FileMetadata fileMetadata = storageService.store(file);

        String imageUrls = fileMetadata.getFilename();

        Evento evento = Evento.builder()
                .name(createEventRequest.name())
                .date(createEventRequest.date())
                .capacity(createEventRequest.capacity())
                .file(getImageUrl(imageUrls))
                .build();

        return eventoRepository.save(evento);
    }

    public String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
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

    @Transactional
    public Ticket comprarTicket(String eventName, UUID usuarioId) {
        Usuario usuario = userRepository.findByIdWithEvents(usuarioId)
                .orElseThrow(() -> new UsersNotFoundException("Usuario no encontrado"));

        Evento evento = eventoRepository.findByName(eventName)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado"));

        if (evento.getAttendees().contains(usuario)) {
            throw new RuntimeException("El usuario ya está registrado en este evento.");
        }

        Ticket ticket = Ticket.builder()
                .event(evento)
                .user(usuario)
                .build();

        ticketRepository.save(ticket);

        evento.addAttendee(usuario);
        eventoRepository.save(evento);

        return ticket;
    }



}
