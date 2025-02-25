package com.example.ApiArenaXperience.controller.review;

import com.example.ApiArenaXperience.model.review.Review;
import com.example.ApiArenaXperience.service.review.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Tag(name = "Reseñas", description = "Endpoints para gestión de reseñas")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/agregar")
    public ResponseEntity<Review> crearReview(
            @RequestParam String username,
            @RequestParam UUID eventoId,
            @RequestParam int rating,
            @RequestParam String comment) {

        Review review = reviewService.crearReview(username, eventoId, rating, comment);
        return ResponseEntity.ok(review);
    }
}
