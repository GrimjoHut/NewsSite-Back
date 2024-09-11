package com.example.testproject.controllers;

import com.example.testproject.models.entities.User;
import com.example.testproject.models.models.Dto.UserDto;
import com.example.testproject.models.models.requests.LoginRequest;
import com.example.testproject.models.models.requests.UserRequest;
import com.example.testproject.services.AuthService;
import com.example.testproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.testproject.security.JWTResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody UserRequest userRequest){
        User user = authService.createUser(userRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserDto.mapFromEntity(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok().body("Good bye");
    }
}
