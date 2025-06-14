package com.example.ApiArenaXperience.service.event;


import com.example.ApiArenaXperience.dto.event.CreateEventRequest;
import com.example.ApiArenaXperience.dto.event.EditEventoCmd;
import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.dto.ticket.TicketWithUserResponse;
import com.example.ApiArenaXperience.error.event.EventNotFoundException;
import com.example.ApiArenaXperience.error.ticket.TicketsNotFoundException;
import com.example.ApiArenaXperience.error.user.UserRoleException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    public Page<EventoResponse> getAllEvents(Pageable pageable) {
        Page<Evento> events = eventoRepository.findAll(pageable);


        if (events.isEmpty()) {
            throw new EventNotFoundException("No hay eventos creados.");
        }


        return events.map(EventoResponse::of);
    }


    @Transactional
    public List<TicketWithUserResponse> getTicketsForEvent(String eventName) {
        Optional<Evento> evento = eventoRepository.findByName(eventName);


        if (evento.isEmpty()){
            throw new EventNotFoundException("No se han encontrado eventos");
        }


        Set<Ticket> tickets = evento.get().getTickets();


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
                .price(createEventRequest.price())
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
            throw new EventNotFoundException("Evento no encontrado con nombre: " + name);
        }


        Evento evento = optionalEvento.get();
        evento.setDate(editEventoCmd.date());
        evento.setCapacity(editEventoCmd.capacity());


        return eventoRepository.save(evento);
    }


    @Transactional
    public void deleteEvent(String name) {
        Optional<Evento> evento = eventoRepository.findByName(name);


        if (evento.isEmpty()) {
            throw new EventNotFoundException("No se ha encontrado el evento");
        }


        if (!evento.get().getTickets().isEmpty()) {
            throw new TicketsNotFoundException("No se puede eliminar un evento con tickets asociados");
        }


        eventoRepository.delete(evento.get());
    }


    @Transactional
    public Ticket comprarTicket(String eventName, UUID usuarioId, int cantidad) {
        Optional<Usuario> usuario = userRepository.findByIdWithEvents(usuarioId);
        Optional<Evento> evento = eventoRepository.findByName(eventName);

        if (usuario.isEmpty()) {
            throw new UsersNotFoundException("Usuario no encontrado");
        }

        if (evento.isEmpty()) {
            throw new EventNotFoundException("Evento no encontrado");
        }

        Evento ev = evento.get();

        int ticketsComprados = ev.getTickets().stream()
                .mapToInt(Ticket::getCantidad)
                .sum();

        if ((ticketsComprados + cantidad) > ev.getCapacity()) {
            throw new RuntimeException("No hay suficientes entradas disponibles.");
        }

        double precioFinal = ev.getPrice() * cantidad;

        Ticket ticket = Ticket.builder()
                .event(ev)
                .user(usuario.get())
                .cantidad(cantidad)
                .precioFinal(precioFinal)
                .build();

        ticketRepository.save(ticket);

        ev.setPrecioTotalRecaudado(ev.getPrecioTotalRecaudado() + precioFinal);

        if (!ev.getAttendees().contains(usuario.get())) {
            ev.addAttendee(usuario.get());
        }

        eventoRepository.save(ev);

        return ticket;
    }




    @Transactional
    public void eliminarTicket(UUID ticketId, UUID usuarioId, boolean isAdmin) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);


        if (ticketOptional.isEmpty()) {
            throw new TicketsNotFoundException("No se ha encontrado el ticket con ID: " + ticketId);
        }


        Ticket ticket = ticketOptional.get();


        if (!isAdmin && !ticket.getUser().getId().equals(usuarioId)) {
            throw new UserRoleException("No tienes permisos para eliminar este ticket.");
        }


        Evento evento = ticket.getEvent();
        Usuario usuario = ticket.getUser();


        evento.removeAttendee(usuario);
        eventoRepository.save(evento);


        ticketRepository.delete(ticket);
    }


}
