package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
