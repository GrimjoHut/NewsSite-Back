package com.example.testproject.controllers;

import com.example.testproject.models.DTO.LoginDTO;
import com.example.testproject.services.UserService;
import com.example.testproject.services.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.testproject.security.JWTResponse;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> createNewUser(@RequestBody LoginDTO loginDTO){
        return userService.createUser(loginDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok().body("Good bye");
    }

    @GetMapping("/verify")
    public ResponseEntity verifyAccount(@RequestParam("token") String token) {
        return  userService.verifyUser(token);
    }
}
