package com.example.testproject.controllers;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.FriendStatusEnum;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.models.models.Dto.UserDto;
import com.example.testproject.models.models.requests.LoginRequest;
import com.example.testproject.services.CustomUserDetailsService;
import com.example.testproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/verify")
    public ResponseEntity<UserDto> verifyAccount(@RequestParam("token") String token) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserDto.mapFromEntity(userService.verifyUser(token)));
    }

    @Secured("ROLE_USER")
    @GetMapping("/subscribers")
    public ResponseEntity<Page<UserDto>> subscribersToCommunity(@RequestParam Long communityId,
                                                               @PageableDefault Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findByCommunity(communityId, pageable)
                        .map(UserDto::mapFromEntitySimplify)
                );
    }

    @Secured("ROLE_USER")
    @PutMapping(value = "/newRequest", consumes = {"multipart/form-data"})
    public ResponseEntity<UserDto> changeAvatar(@RequestPart("files") MultipartFile file,
                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserDto.mapFromEntity(
                        userService.changeAvatar(file, userDetails)
                ));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/giveRole")
    public ResponseEntity<UserDto> giveRole(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestParam Long userId,
                                           @RequestParam RoleEnum role) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserDto.mapFromEntity
                        (userService.giveRole(userDetails, userId, role)));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/takeRole")
    public ResponseEntity<UserDto> takeRole(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestParam Long userId,
                                           @RequestParam RoleEnum role) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserDto.mapFromEntity
                        (userService.takeRole(userDetails, userId, role)));
    }
}
