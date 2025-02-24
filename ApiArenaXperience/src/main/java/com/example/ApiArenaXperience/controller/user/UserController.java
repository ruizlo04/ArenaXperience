package com.example.ApiArenaXperience.controller.user;

import com.example.ApiArenaXperience.dto.ticket.TicketResponse;
import com.example.ApiArenaXperience.dto.user.*;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.security.jwt.access.JwtService;
import com.example.ApiArenaXperience.security.jwt.refresh.RefreshToken;
import com.example.ApiArenaXperience.security.jwt.refresh.RefreshTokenRequest;
import com.example.ApiArenaXperience.security.jwt.refresh.RefreshTokenService;
import com.example.ApiArenaXperience.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Registrar un usuario", description = "Crea un nuevo usuario en la aplicación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos")
    })
    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid CreateUserRequest createUserRequest) {
        Usuario user = userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.of(user));
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve tokens de acceso y refresco")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario autenticado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario user = (Usuario) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));
    }

    @Operation(summary = "Refrescar token", description = "Genera un nuevo token de acceso utilizando un refresh token válido.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Nuevo token de acceso generado correctamente"),
            @ApiResponse(responseCode = "400", description = "Refresh token inválido o expirado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping("/auth/refresh/token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest req) {
        String token = req.refreshToken();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));
    }

    @Operation(summary = "Activar cuenta", description = "Activa la cuenta de un usuario con un token de activación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta activada correctamente"),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @PostMapping("/activate/account/")
    public ResponseEntity<?> activateAccount(@RequestBody ActivateAccountRequest req) {
        String token = req.token();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(userService.activateAccount(token)));
    }

    @Operation(summary = "Obtener usuario autenticado", description = "Obtiene los datos del usuario autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos del usuario obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal Usuario user) {
        return UserResponse.of(user);
    }

    @Operation(summary = "Obtener usuario autenticado (Admin)", description = "Obtiene los datos del usuario autenticado con más detalles (solo para administradores)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos del usuario obtenidos correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido para usuarios no administradores")
    })
    @GetMapping("/me/admin")
    public Usuario adminMe(@AuthenticationPrincipal Usuario user) {
        return user;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Editar un usuario", description = "Permite a un usuario autenticado modificar su información de perfil.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDto.class))),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para editar este usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{username}")
    public GetUserDto editUser(
            @PathVariable String username,
            @RequestBody @Valid EditUserCmd editUserCmd,
            @AuthenticationPrincipal Usuario user) {
        String authenticatedUsername = user.getUsername();
        Usuario updatedUser = userService.editUser(username, editUserCmd, authenticatedUsername);
        return GetUserDto.of(updatedUser);
    }

    @Operation(summary = "Editar un usuario", description = "Permite a un usuario autenticado modificar su información de perfil.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDto.class))),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para editar este usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{username}")
    public GetUserDto editUserByAdmin(
            @PathVariable String username,
            @RequestBody @Valid EditUserCmd editUserCmd) {
        Usuario updatedUser = userService.editUserByAdmin(username, editUserCmd);
        return GetUserDto.of(updatedUser);
    }

    @Operation(summary = "Eliminar un usuario",
            description = "Permite a un usuario autenticado eliminar su propia cuenta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para eliminar este usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "Nombre de usuario del usuario a eliminar", required = true)
            @PathVariable String username,
            @AuthenticationPrincipal Usuario user) {

        userService.deleteUser(username, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar un usuario",
            description = "Permite a un usuario autenticado eliminar su propia cuenta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para eliminar este usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/users/admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByAdmin(@PathVariable String username) {
        userService.deleteUserByAdmin(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/tickets")
    public ResponseEntity<List<TicketResponse>> getUserTickets(
            @PathVariable String username,
            @AuthenticationPrincipal Usuario authenticatedUser
    ) {
        List<TicketResponse> tickets = userService.getUserTickets(username, authenticatedUser);
        return ResponseEntity.ok(tickets);
    }

}
