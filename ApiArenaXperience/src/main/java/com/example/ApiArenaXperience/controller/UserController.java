package com.example.ApiArenaXperience.controller;

import com.example.ApiArenaXperience.dto.CreateUserRequest;
import com.example.ApiArenaXperience.dto.UserResponse;
import com.example.ApiArenaXperience.model.Usuario;
import com.example.ApiArenaXperience.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest createUserRequest) {
        Usuario user = userService.createUser(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

}
