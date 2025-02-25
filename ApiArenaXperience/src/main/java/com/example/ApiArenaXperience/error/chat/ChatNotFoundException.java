package com.example.ApiArenaXperience.error.chat;

import com.example.ApiArenaXperience.error.EntidadNotFound;
import org.springframework.http.HttpStatus;

public class ChatNotFoundException extends EntidadNotFound {
    public ChatNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
