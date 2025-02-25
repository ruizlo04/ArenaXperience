package com.example.ApiArenaXperience.dto.review;

import com.example.ApiArenaXperience.model.review.Review;

public record EditResenyaCmd(
        int rating,
        String comment
) {

    public static EditResenyaCmd of(Review review){
        return new EditResenyaCmd(
                review.getRating(),
                review.getComment()
        );
    }
}
