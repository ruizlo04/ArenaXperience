package com.example.ApiArenaXperience.controller.event;

import com.example.ApiArenaXperience.dto.event.CreateEventRequest;
import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.dto.event.GetListEventoFilterDto;
import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.service.event.EventoService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
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
    @GetMapping("/events")
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
    @GetMapping("/events/search")
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
    @PostMapping("/evento/register")
    public ResponseEntity<EventoResponse> register(@RequestBody @Valid CreateEventRequest createEventRequest) {
        Evento evento = eventoService.createEvent(createEventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(EventoResponse.of(evento));
    }

}
