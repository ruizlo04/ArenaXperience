package com.example.ApiArenaXperience.controller.event;

import com.example.ApiArenaXperience.dto.event.*;
import com.example.ApiArenaXperience.dto.ticket.GetTicketDto;
import com.example.ApiArenaXperience.dto.ticket.TicketWithUserResponse;
import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.model.ticket.Ticket;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.service.event.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/evento")
@Tag(name = "Eventos", description = "Endpoints para gestión de eventos")
public class EventoController {

    private final EventoService eventoService;

    @Operation(summary = "Obtener todos los eventos", description = "Devuelve una lista de todos los eventos registrados en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de eventos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventoResponse.class))),
            @ApiResponse(responseCode = "404", description = "No hay eventos disponibles")
    })
    @GetMapping("/")
    public ResponseEntity<List<EventoResponse>> getAllEvents() {
        return ResponseEntity.ok(eventoService.getAllEvents());
    }

    @Operation(summary = "Buscar eventos con filtros", description = "Permite buscar eventos filtrando por nombre, fecha o capacidad. " +
            "Si se proporciona al menos un filtro, se devolverán los eventos coincidentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de eventos filtrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetListEventoFilterDto.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron eventos con los filtros aplicados"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, verifique los parámetros enviados")
    })
    @GetMapping("/search")
    public ResponseEntity<List<GetListEventoFilterDto>> searchEvents(
            @RequestBody @Valid GetListEventoFilterDto filter) {

        List<GetListEventoFilterDto> eventos = eventoService.buscarEventos(filter.name(), filter.date(), filter.capacity())
                .stream()
                .map(evento -> new GetListEventoFilterDto(
                        evento.getName(),
                        evento.getDate(),
                        evento.getCapacity()
                ))
                .toList();

        if (eventos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(eventos);
    }

    @Operation(summary = "Registrar un evento", description = "Crea un nuevo evento en la aplicación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/register")
    public ResponseEntity<EventoResponse> register(
            @RequestPart("event") @Valid CreateEventRequest createEventRequest,
            @RequestPart("file") MultipartFile file) {

        Evento evento = eventoService.createEvent(createEventRequest, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(EventoResponse.of(evento));
    }


    @Operation(summary = "Editar un evento", description = "Permite modificar la fecha y la capacidad de un evento existente por su nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento editado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editar/{name}")
    public ResponseEntity<GetEventoDto> editarEvento(@PathVariable String name, @RequestBody @Valid EditEventoCmd editEventoCmd) {
        Evento eventoEditado = eventoService.editarEvento(name, editEventoCmd);
        return ResponseEntity.ok(GetEventoDto.of(eventoEditado));
    }

    @Operation(summary = "Eliminar un evento", description = "Permite al administrador del evento eliminarlo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para eliminar este evento"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{name}")
    public ResponseEntity<?> eliminarEvento(@PathVariable String name) {
        eventoService.deleteEvent(name);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Comprar un ticket para un evento", description = "Permite a un usuario autenticado comprar un ticket para un evento específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket comprado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
            @ApiResponse(responseCode = "400", description = "El usuario ya está registrado en el evento")
    })
    @PostMapping("/{eventName}/comprar-ticket")
    public ResponseEntity<GetTicketDto> comprarTicket(
            @PathVariable String eventName,
            @AuthenticationPrincipal Usuario usuario) {
        Ticket ticket = eventoService.comprarTicket(eventName, usuario.getId());
        return ResponseEntity.ok(GetTicketDto.of(ticket));
    }

    @Operation(
            summary = "Obtener tickets de un evento",
            description = "Recupera la lista de tickets asociados a un evento específico. "
                    + "Solo los administradores pueden acceder a esta información."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tickets obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketWithUserResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requieren permisos de administrador"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{eventName}/tickets")
    public ResponseEntity<List<TicketWithUserResponse>> getTicketsForEvent(
            @Parameter(description = "Nombre del evento para obtener los tickets", required = true)
            @PathVariable String eventName
    ) {
        List<TicketWithUserResponse> tickets = eventoService.getTicketsForEvent(eventName);
        return ResponseEntity.ok(tickets);
    }



}
