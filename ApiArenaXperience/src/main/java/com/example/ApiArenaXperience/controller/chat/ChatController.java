package com.example.ApiArenaXperience.controller.chat;

import com.example.ApiArenaXperience.dto.chat.CreateChatDto;
import com.example.ApiArenaXperience.model.chat.Chat;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

}