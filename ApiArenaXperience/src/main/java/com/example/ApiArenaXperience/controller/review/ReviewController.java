package com.example.ApiArenaXperience.controller.review;

import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.dto.review.EditResenyaCmd;
import com.example.ApiArenaXperience.dto.review.ReviewRequestDto;
import com.example.ApiArenaXperience.dto.review.ReviewResponseDto;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.service.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Tag(name = "Reseñas", description = "Endpoints para gestión de reseñas")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Crear una reseña",
            description = "Permite a los usuarios con rol de SOCIO crear una reseña para un evento específico. " +
                    "La reseña incluye una calificación (rating) y un comentario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Reseña creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado. Solo los usuarios con rol de SOCIO pueden crear reseñas",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/agregar")
    public ResponseEntity<ReviewResponseDto> crearReview(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la reseña a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewRequestDto.class),
                            examples = @ExampleObject(value = """
                                {
                                    "eventoName": "Concierto de Rock",
                                    "rating": 5,
                                    "comment": "¡Un evento increíble! La banda estuvo genial."
                                }
                                """)
                    )
            )
            @RequestBody @Valid ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto reviewResponseDto = reviewService.crearReview(reviewRequestDto);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @Operation(summary = "Obtener reseñas por evento",
            description = "Recupera una lista paginada de reseñas para un evento específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reseñas recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/evento/{eventoName}")
    public Page<ReviewResponseDto> obtenerResenasPorEvento(
            Pageable pageable,
            @PathVariable String eventoName) {
        return reviewService.obtenerResenasPorEvento(pageable, eventoName);
    }

    @Operation(summary = "Obtener todas las reseñas",
            description = "Recupera una lista paginada de todas las reseñas disponibles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reseñas recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDto.class))
            )
    })
    @GetMapping("/evento/")
    public Page<ReviewResponseDto> obtenerTodasLasResenas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.obtenerTodasLasResenas(pageable);
    }

    @Operation(summary = "Editar una reseña",
            description = "Permite a los usuarios editar la calificación y el comentario de una reseña existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña editada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/editar/{id}")
    public ResponseEntity<Optional<ReviewResponseDto>> editarResenya(
            @PathVariable UUID id,
            @RequestBody EditResenyaCmd editResenyaCmd,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        Optional<ReviewResponseDto> updatedReview = reviewService.editarResenya(id, editResenyaCmd, usuarioAutenticado);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(summary = "Eliminar una reseña",
            description = "Permite a los usuarios eliminar una reseña específica. Solo el propietario de la reseña o un usuario con rol ADMIN pueden realizar esta acción.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Solo el propietario o un administrador pueden eliminar la reseña",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarResenya(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        reviewService.eliminarResenya(id, usuarioAutenticado);
        return ResponseEntity.noContent().build();
    }


}
