package com.example.ApiArenaXperience.dto.user;

import com.example.ApiArenaXperience.model.Usuario;


public record GetUserDto(
        String username,
        String password,
        String email,
        String phoneNumber
) {

    public static GetUserDto of (Usuario u){
        return new GetUserDto(
                u.getUsername(),
                u.getPassword(),
                u.getEmail(),
                u.getPhoneNumber()
        );
    }
}
