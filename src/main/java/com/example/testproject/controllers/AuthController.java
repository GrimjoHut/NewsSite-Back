package com.example.testproject.controllers;

import com.example.testproject.models.models.requests.LoginRequest;
import com.example.testproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.testproject.security.JWTResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> createNewUser(@RequestBody LoginRequest loginRequest){
        userService.createUser(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Please check your email for verification code");
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok().body("Good bye");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        userService.verifyUser(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Verification completed");
    }
}
