package com.example.ApiArenaXperience.service.chat;


import com.example.ApiArenaXperience.error.user.UsersNotFoundException;
import com.example.ApiArenaXperience.model.chat.Chat;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.example.ApiArenaXperience.repo.ChatRepository;
import com.example.ApiArenaXperience.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public Chat sendMessage(UUID senderId, String receiverUsername, String message) {
        Optional<Usuario> sender = userRepository.findById(senderId);
        if (sender.isEmpty()){
            throw new UsersNotFoundException("No se ha encontrado el usuario para enviar el mensaje");
        }

        Optional<Usuario> receiver = userRepository.findFirstByUsername(receiverUsername);
        if (receiver.isEmpty()){
            throw new UsersNotFoundException("No se ha encontrado el usuario al que le debe llegar el mensaje");
        }

        Chat chat = Chat.builder()
                .sender(sender.get())
                .receiver(receiver.get())
                .message(message)
                .sentAt(LocalDateTime.now())
                .build();

        chat = chatRepository.save(chat);

        return chat;
    }

    @Transactional(readOnly = true)
    public Page<Chat> getUserChats(UUID userId, Pageable pageable) {
        Optional<Usuario> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UsersNotFoundException("No existe dicho usuario");
        }

        return chatRepository.findBySenderOrReceiver(user.get(), user.get(), pageable);
    }


}