package com.example.ApiArenaXperience.service.user;

import com.example.ApiArenaXperience.dto.user.CreateUserRequest;
import com.example.ApiArenaXperience.dto.user.EditUserCmd;
import com.example.ApiArenaXperience.dto.user.UserResponse;
import com.example.ApiArenaXperience.error.ActivationExpiredException;
import com.example.ApiArenaXperience.error.user.UsersNotFoundException;
import com.example.ApiArenaXperience.model.user.UserRole;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.repo.UserRepository;
import com.example.ApiArenaXperience.security.jwt.refresh.RefreshTokenRepository;
import com.example.ApiArenaXperience.security.util.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${activation.duration}")
    private int activationDuration;

    public Usuario createUser(CreateUserRequest createUserRequest) {

        Usuario user = Usuario.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .activationToken(generateRandomActivationCode())
                .createdAt(Instant.now())
                .enabled(false)
                .roles(Collections.singleton(createUserRequest.rol() != null ? createUserRequest.rol() : UserRole.USER))
                .build();

        try {
            mailService.sendSimpleMessage(createUserRequest.email(), user.getActivationToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el email de activación");
        }

        return userRepository.save(user);
    }


    public String generateRandomActivationCode() {
        return UUID.randomUUID().toString();
    }

    public Usuario activateAccount(String token) {
        return userRepository.findByActivationToken(token)
                .filter(user -> ChronoUnit.MINUTES.between(user.getCreatedAt(), Instant.now()) < activationDuration)
                .map(user -> {
                    user.setEnabled(true);
                    user.setActivationToken(null);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ActivationExpiredException("El código de activación no existe o ha caducado"));
    }
    public List<UserResponse> getAllUsers() {
        List<Usuario> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new UsersNotFoundException("No hay usuarios registrados en el sistema.");
        }

        return users.stream()
                .map(UserResponse::of)
                .collect(Collectors.toList());
    }

    public Usuario editUser(String username, EditUserCmd editUserCmd, String authenticatedUsername) {
        if (!authenticatedUsername.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para editar este usuario");
        }

        Usuario userToEdit = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsersNotFoundException("Usuario no encontrado"));

        userToEdit.setEmail(editUserCmd.email());
        userToEdit.setPassword(editUserCmd.password());
        userToEdit.setPhoneNumber(editUserCmd.phoneNumber());

        return userRepository.save(userToEdit);
    }

    public Usuario editUserByAdmin(String username, EditUserCmd editUserCmd) {


        Usuario userToEdit = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsersNotFoundException("Usuario no encontrado"));


        userToEdit.setEmail(editUserCmd.email());
        userToEdit.setPassword(editUserCmd.password());
        userToEdit.setPhoneNumber(editUserCmd.phoneNumber());


        return userRepository.save(userToEdit);
    }


    @Transactional
    public void deleteUser(String username, String authenticatedUsername) {
        if (!authenticatedUsername.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para eliminar este usuario");
        }

        Usuario userToDelete = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsersNotFoundException("Usuario no encontrado"));

        refreshTokenRepository.deleteByUserId(userToDelete.getId());

        userRepository.delete(userToDelete);
    }

    @Transactional
    public void deleteUserByAdmin(String username) {
        Usuario userToDelete = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new UsersNotFoundException("Usuario no encontrado"));

        refreshTokenRepository.deleteByUserId(userToDelete.getId());
        userRepository.delete(userToDelete);
    }


}
