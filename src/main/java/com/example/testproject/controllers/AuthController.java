package com.example.testproject.controllers;

import com.example.testproject.models.DTO.LoginDTO;
import com.example.testproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.testproject.security.JWTResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity createNewUser(@RequestBody LoginDTO loginDTO){
        return userService.createUser(loginDTO);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
}
