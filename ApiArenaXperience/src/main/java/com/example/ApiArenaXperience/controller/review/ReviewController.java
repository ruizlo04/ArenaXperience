package com.example.ApiArenaXperience.controller.review;

import com.example.ApiArenaXperience.dto.event.EventoResponse;
import com.example.ApiArenaXperience.dto.review.ReviewRequestDto;
import com.example.ApiArenaXperience.dto.review.ReviewResponseDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
