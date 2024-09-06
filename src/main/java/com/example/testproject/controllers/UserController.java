package com.example.testproject.controllers;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.models.models.Dto.UserDto;
import com.example.testproject.services.CustomUserDetailsService;
import com.example.testproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @Secured("ROLE_USER")
    @PostMapping(value = "/newRequest", consumes = {"multipart/form-data"})
    public ResponseEntity<String> changeAvatar(@RequestPart("files") MultipartFile file,
                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        userService.changeAvatar(file, userDetails);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Avatar changed");
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
