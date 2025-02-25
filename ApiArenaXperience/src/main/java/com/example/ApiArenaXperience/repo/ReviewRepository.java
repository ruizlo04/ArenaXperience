package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.model.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByEvent(Pageable pageable, Evento evento);
}
