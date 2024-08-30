package com.example.testproject.controllers;

import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/giveRole")
    public ResponseEntity<String> giveRole(@RequestParam String actingUser, @RequestParam String nickname, @RequestParam RoleEnum role) {
        return userService.giveRole(actingUser, nickname, role);
    }

    @PutMapping("/takeRole")
    public ResponseEntity<String> takeRole(@RequestParam String actingUser, @RequestParam String nickname, @RequestParam RoleEnum role) {
        return userService.takeRole(actingUser, nickname, role);
    }
}
