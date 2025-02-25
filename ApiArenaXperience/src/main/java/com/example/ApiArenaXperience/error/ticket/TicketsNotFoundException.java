package com.example.ApiArenaXperience.error.ticket;

import com.example.ApiArenaXperience.error.EntidadNotFound;
import org.springframework.http.HttpStatus;

public class TicketsNotFoundException extends EntidadNotFound {
    public TicketsNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
