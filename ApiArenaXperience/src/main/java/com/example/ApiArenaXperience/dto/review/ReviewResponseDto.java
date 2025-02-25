package com.example.ApiArenaXperience.dto.review;


import com.example.ApiArenaXperience.model.review.Review;

import java.util.UUID;

public record ReviewResponseDto(
        UUID id,
        String username,
        String eventoName,
        int rating,
        String comment
) {
    public static ReviewResponseDto of (Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getUsername(),
                review.getEvent().getName(),
                review.getRating(),
                review.getComment()
        );
    }
}
