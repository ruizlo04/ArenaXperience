package com.example.ApiArenaXperience.service.user;

import com.example.ApiArenaXperience.dto.ticket.TicketResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<Usuario> users = userRepository.findAll(pageable);

        if (users.isEmpty()) {
            throw new UsersNotFoundException("No hay usuarios registrados en el sistema.");
        }

        return users.map(UserResponse::of);
    }

    public Usuario createUser(CreateUserRequest createUserRequest) {

        Usuario user = Usuario.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .email(createUserRequest.email())
                .phoneNumber(createUserRequest.phoneNumber())
                .phoneNumber(createUserRequest.phoneNumber())
                .activationToken(generateRandomActivationCode())
                .createdAt(Instant.now())
                .enabled(false)
                .roles(Collections.singleton(UserRole.USER))
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

    public List<TicketResponse> getUserTickets(String username, Usuario authenticatedUser) {
        if (!authenticatedUser.getRoles().contains(UserRole.ADMIN) && !authenticatedUser.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para ver estos tickets");
        }

        Optional<Usuario> user = userRepository.findByUsernameWithTickets(username);

        if (user.isEmpty()){
            throw new UsersNotFoundException("No se han encontrado tickets para ese usuario");
        }

        return user.get().getTickets().stream()
                .map(TicketResponse::of)
                .collect(Collectors.toList());
    }

    public Usuario editUser(String username, EditUserCmd editUserCmd, String authenticatedUsername) {
        if (!authenticatedUsername.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para editar este usuario");
        }

        Optional<Usuario> userToEdit = userRepository.findFirstByUsername(username);

        if (userToEdit.isEmpty()) {
            throw new UsersNotFoundException("No se ha encontrado ese Usuario");
        }

        Usuario user = userToEdit.get();

        if (!editUserCmd.email().equals(editUserCmd.verifyEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los emails no coinciden");
        }

        String pwd = editUserCmd.password();
        String verifyPwd = editUserCmd.verifyPassword();
        if (pwd != null && !pwd.isBlank()) {
            if (!pwd.equals(verifyPwd)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
            }

            if (pwd.length() < 8 || pwd.length() > 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe tener entre 8 y 20 caracteres");
            }
            if (!pwd.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-@$!%*?&])[A-Za-z\\d-@$!%*?&]{8,}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe contener mayúscula, minúscula, número y carácter especial");
            }

            user.setPassword(passwordEncoder.encode(pwd));
        }

        user.setEmail(editUserCmd.email());
        user.setPhoneNumber(editUserCmd.phoneNumber());

        return userRepository.save(user);
    }

    public Usuario editUserByAdmin(String username, EditUserCmd editUserCmd) {
        Optional<Usuario> userToEdit = userRepository.findFirstByUsername(username);

        if (userToEdit.isEmpty()) {
            throw new UsersNotFoundException("No se ha encontrado ese Usuario");
        }

        Usuario user = userToEdit.get();

        // Validación de emails
        if (!editUserCmd.email().equals(editUserCmd.verifyEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los emails no coinciden");
        }

        // Validación de contraseña
        String pwd = editUserCmd.password();
        String verifyPwd = editUserCmd.verifyPassword();
        if (pwd != null && !pwd.isBlank()) {
            if (!pwd.equals(verifyPwd)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
            }

            if (pwd.length() < 8 || pwd.length() > 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe tener entre 8 y 20 caracteres");
            }

            if (!pwd.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-@$!%*?&])[A-Za-z\\d-@$!%*?&]{8,}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe contener mayúscula, minúscula, número y carácter especial");
            }

            user.setPassword(passwordEncoder.encode(pwd));
        }

        // Asignar nuevos valores
        user.setEmail(editUserCmd.email());
        user.setPhoneNumber(editUserCmd.phoneNumber());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username, String authenticatedUsername) {
        if (!authenticatedUsername.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para eliminar este usuario");
        }

        Optional<Usuario> userToDelete = userRepository.findFirstByUsername(username);

        if (userToDelete.isEmpty()){
            throw new UsersNotFoundException("No se ha encontrado ese Usuario");
        }

        refreshTokenRepository.deleteByUserId(userToDelete.get().getId());

        userRepository.delete(userToDelete.get());
    }

    @Transactional
    public void deleteUserByAdmin(String username) {
        Optional<Usuario> userToDelete = userRepository.findFirstByUsername(username);

        if (userToDelete.isEmpty()){
            throw new UsersNotFoundException("No se ha encontrado ese Usuario");
        }

        refreshTokenRepository.deleteByUserId(userToDelete.get().getId());
        userRepository.delete(userToDelete.get());
    }


}
