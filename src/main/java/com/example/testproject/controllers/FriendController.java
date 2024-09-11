package com.example.testproject.controllers;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.entities.Friends;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.models.Dto.UserDto;
import com.example.testproject.services.FriendsService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendsService friendsService;

    @PutMapping("/addFriend")
    @Secured("ROLE_USER")
    public ResponseEntity<Friends> addFriend(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestParam Long userId){
        return ResponseEntity.ok(friendsService.addFriend(userDetails, userId));
    }

    @Secured("ROLE_USER")
    @GetMapping("friends")
    public ResponseEntity<Page<UserDto>> userFriends(@RequestParam Long userId,
                                                     @PageableDefault Pageable pageable){
        Page<User> friends = friendsService.findFriends(userId, pageable);
        Page<UserDto> friendsDtos = friends.map(UserDto::mapFromEntitySimplify);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(friendsDtos);
    }

    @PutMapping("/acceptFriend")
    @Secured("ROLE_USER")
    public ResponseEntity<Friends> acceptFriend(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestParam Long userId){
        return ResponseEntity.ok(friendsService.acceptFriend(userDetails, userId));
    }

    @DeleteMapping("/deleteFriendRequest")
    @Secured("ROLE_USER")
    public ResponseEntity<String> deleteFriend(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestParam Long userId){
        friendsService.deleteFriendRequest(userDetails, userId);
        return ResponseEntity.ok("Friend request deleted");
    }
}
