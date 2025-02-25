package com.example.ApiArenaXperience.error.review;

import com.example.ApiArenaXperience.error.EntidadNotFound;
import org.springframework.http.HttpStatus;

public class ResenyaNotFoundException extends EntidadNotFound {
    public ResenyaNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
