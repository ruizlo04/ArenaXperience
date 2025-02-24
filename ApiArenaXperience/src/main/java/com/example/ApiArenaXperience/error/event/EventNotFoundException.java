package com.example.ApiArenaXperience.error.event;

import com.example.ApiArenaXperience.error.EntidadNotFound;
import org.springframework.http.HttpStatus;

public class EventNotFoundException extends EntidadNotFound {
    public EventNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
