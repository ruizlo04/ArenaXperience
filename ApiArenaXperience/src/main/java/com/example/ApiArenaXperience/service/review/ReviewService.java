package com.example.ApiArenaXperience.service.review;

import com.example.ApiArenaXperience.error.user.UsersNotFoundException;
import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.model.review.Review;
import com.example.ApiArenaXperience.model.user.UserRole;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.repo.EventRepository;
import com.example.ApiArenaXperience.repo.ReviewRepository;
import com.example.ApiArenaXperience.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.example.ApiArenaXperience.model.user.UserRole.SOCIO;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserRepository usuarioRepository;

    private final EventRepository eventoRepository;

    @Transactional
    public Review crearReview(String username, UUID eventoId, int rating, String comment) {
        Optional<Usuario> usuario = usuarioRepository.findFirstByUsername(username);

        if (usuario.isEmpty()){
            throw new UsersNotFoundException("No existe usuario con ese nombre");
        }
        if (usuario.get().getRoles().contains(SOCIO)) {
            throw new UsersNotFoundException("Solo los socios pueden crear reseñas");
        }

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        Review review = Review.builder()
                .user(usuario)
                .event(evento)
                .rating(rating)
                .comment(comment)
                .build();

        return reviewRepository.save(review);
    }
}
