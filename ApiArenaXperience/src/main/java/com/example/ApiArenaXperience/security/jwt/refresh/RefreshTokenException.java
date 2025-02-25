package com.example.ApiArenaXperience.security.jwt.refresh;

import com.example.ApiArenaXperience.security.exceptionhandling.JwtException;

public class RefreshTokenException extends JwtException {
    public RefreshTokenException(String s) {
        super(s);
    }
}
