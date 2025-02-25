package com.example.ApiArenaXperience.service.review;

import com.example.ApiArenaXperience.dto.review.EditResenyaCmd;
import com.example.ApiArenaXperience.dto.review.ReviewRequestDto;
import com.example.ApiArenaXperience.dto.review.ReviewResponseDto;
import com.example.ApiArenaXperience.error.event.EventNotFoundException;
import com.example.ApiArenaXperience.error.review.ResenyaNotFoundException;
import com.example.ApiArenaXperience.error.user.UserRoleException;
import com.example.ApiArenaXperience.error.user.UsersNotFoundException;
import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.model.review.Review;
import com.example.ApiArenaXperience.model.user.UserRole;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.repo.EventRepository;
import com.example.ApiArenaXperience.repo.ReviewRepository;
import com.example.ApiArenaXperience.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserRepository usuarioRepository;

    private final EventRepository eventoRepository;

    @Transactional
    public ReviewResponseDto crearReview(ReviewRequestDto reviewRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Usuario> usuario = usuarioRepository.findFirstByUsername(username);

        if (usuario.isEmpty()){
            throw new UsersNotFoundException("Usuario no encontrado");
        }

        if (!usuario.get().getRoles().contains(UserRole.SOCIO)) {
            throw new UserRoleException("Solo los socios pueden crear reseñas");
        }

        Optional<Evento> evento = eventoRepository.findByName(reviewRequestDTO.eventoName());

        if (evento.isEmpty()){
            throw new EventNotFoundException("No se ha encontrado el evento");
        }

        Review review = Review.builder()
                .user(usuario.get())
                .event(evento.get())
                .rating(reviewRequestDTO.rating())
                .comment(reviewRequestDTO.comment())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.of(savedReview);
    }

    @Transactional
    public Page<ReviewResponseDto> obtenerResenasPorEvento(Pageable pageable, String eventoName) {
        Optional<Evento> evento = eventoRepository.findByName(eventoName);

        if (evento.isEmpty()) {
            throw new EventNotFoundException("No se ha encontrado el evento");
        }

        Page<Review> resenyas = reviewRepository.findByEvent(pageable, evento.get());

        return resenyas.map(ReviewResponseDto::of);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> obtenerTodasLasResenas(Pageable pageable) {
        Page<Review> resenyasPage = reviewRepository.findAll(pageable);

        return resenyasPage.map(ReviewResponseDto::of);
    }

    public Optional<ReviewResponseDto> editarResenya(UUID id, EditResenyaCmd editResenyaCmd, Usuario usuarioAutenticado) {
        Optional<Review> reviewOpt = reviewRepository.findById(id);

        if (reviewOpt.isEmpty()) {
            throw new ResenyaNotFoundException("No se ha encontrado esa reseña");
        }

        Review review = reviewOpt.get();

        boolean esPropietario = review.getUser().getId().equals(usuarioAutenticado.getId());
        boolean esAdmin = usuarioAutenticado.getRoles().contains(UserRole.ADMIN);

        if (!esPropietario && !esAdmin) {
            throw new UserRoleException("No tienes permiso para editar esta reseña");
        }

        review.setRating(editResenyaCmd.rating());
        review.setComment(editResenyaCmd.comment());

        reviewRepository.save(review);

        return Optional.of(ReviewResponseDto.of(review));
    }


}
