package com.example.testproject.controllers;

import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.models.models.Dto.UserDto;
import com.example.testproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Secured("ROLE_USER")
    @GetMapping("/profile")
    public ResponseEntity<UserDto> userProfile(@RequestParam Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserDto
                        .mapFromEntity(userService.findById(id)));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/giveRole")
    public ResponseEntity<String> giveRole(@RequestParam String actingUser, @RequestParam String nickname, @RequestParam RoleEnum role) {
        return userService.giveRole(actingUser, nickname, role);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/takeRole")
    public ResponseEntity<String> takeRole(@RequestParam String actingUser, @RequestParam String nickname, @RequestParam RoleEnum role) {
        return userService.takeRole(actingUser, nickname, role);
    }
}
