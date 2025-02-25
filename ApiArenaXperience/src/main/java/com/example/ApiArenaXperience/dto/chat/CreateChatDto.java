package com.example.ApiArenaXperience.dto.chat;

import com.example.ApiArenaXperience.model.chat.Chat;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateChatDto(
        UUID id,
        String senderUsername,
        String receiverUsername,
        String message,
        LocalDateTime sentAt
) {

    public static CreateChatDto of(Chat chat){
        return new CreateChatDto(
                chat.getId(),
                chat.getSender().getUsername(),
                chat.getReceiver().getUsername(),
                chat.getMessage(),
                chat.getSentAt()
        );
    }
}
