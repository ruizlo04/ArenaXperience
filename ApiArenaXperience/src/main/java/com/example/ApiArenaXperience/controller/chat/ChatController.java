package com.example.ApiArenaXperience.controller.chat;

import com.example.ApiArenaXperience.dto.chat.CreateChatDto;
import com.example.ApiArenaXperience.dto.chat.EditChatCmd;
import com.example.ApiArenaXperience.model.chat.Chat;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Endpoints para gestión de mensajes")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "Enviar un mensaje", description = "Envía un mensaje de un usuario a otro.")
    @ApiResponse(responseCode = "200", description = "Mensaje enviado exitosamente")
    @PostMapping("/send")
    public ResponseEntity<CreateChatDto> sendMessage(
            @RequestParam String username,
            @RequestParam String message,
            @AuthenticationPrincipal Usuario sender) {

        UUID senderId = sender.getId();

        Chat chat = chatService.sendMessage(senderId, username, message);

        return ResponseEntity.ok(CreateChatDto.of(chat));
    }

    @Operation(
            summary = "Obtener lista de chats del usuario",
            description = "Recupera todos los chats en los que el usuario autenticado ha participado, ya sea como emisor o receptor."
    )
    @ApiResponse(responseCode = "200", description = "Lista de chats recuperada exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. El usuario debe estar autenticado")
    @GetMapping("/list")
    public ResponseEntity<Page<CreateChatDto>> getUserChats(
            @AuthenticationPrincipal Usuario user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Chat> chatPage = chatService.getUserChats(user.getId(), pageable);
        Page<CreateChatDto> chatDtos = chatPage.map(CreateChatDto::of);

        return ResponseEntity.ok(chatDtos);
    }


    @Operation(summary = "Editar un mensaje", description = "Permite al remitente modificar el contenido de su mensaje.")
    @ApiResponse(responseCode = "200", description = "Mensaje editado exitosamente")
    @PutMapping("/edit/{chatId}")
    public ResponseEntity<CreateChatDto> editMessage(
            @PathVariable UUID chatId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para editar el mensaje", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditChatCmd.class),
                            examples = @ExampleObject(value = """
                                {
                                    "message": "Nuevo contenido del mensaje"
                                }
                                """)))
            @RequestBody @Valid EditChatCmd editChatCmd,
            @AuthenticationPrincipal Usuario sender) {

        Chat chat = chatService.editMessage(sender.getId(), chatId, editChatCmd.message());
        return ResponseEntity.ok(CreateChatDto.of(chat));
    }


    @Operation(summary = "Eliminar un mensaje", description = "Permite a un usuario eliminar un mensaje que haya enviado.")
    @ApiResponse(responseCode = "204", description = "Mensaje eliminado exitosamente")
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable UUID chatId,
            @AuthenticationPrincipal Usuario sender) {

        chatService.deleteMessage(sender.getId(), chatId);
        return ResponseEntity.noContent().build();
    }


}