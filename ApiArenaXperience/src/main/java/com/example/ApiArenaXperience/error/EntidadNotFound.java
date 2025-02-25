package com.example.ApiArenaXperience.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EntidadNotFound extends RuntimeException{

    private final HttpStatus status;

    public EntidadNotFound(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }
}
