package com.example.ApiArenaXperience.error.user;

import com.example.ApiArenaXperience.error.EntidadNotFound;
import org.springframework.http.HttpStatus;

public class UsersNotFoundException extends EntidadNotFound {
    public UsersNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
