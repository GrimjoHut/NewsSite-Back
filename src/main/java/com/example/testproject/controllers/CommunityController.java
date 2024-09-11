package com.example.testproject.controllers;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.entities.Community;
import com.example.testproject.models.models.Dto.CommunityDto;
import com.example.testproject.models.models.requests.CommunityRequest;
import com.example.testproject.services.CommunityService;
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

@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;


    @Secured("ROLE_USER")
    @PostMapping(value = "/newPublic", consumes = {"multipart/form-data"})
    public ResponseEntity<Community> createCommunity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @RequestBody CommunityRequest communityRequest,
                                                     @RequestPart MultipartFile file){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body((communityService.createCommunity(communityRequest, userDetails, file)));
    }

    @Secured("ROLE_USER")
    @PutMapping("/subscribe")
    public ResponseEntity<String> subscribeToCommunity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestParam Long communityId){
        communityService.subscribeToCommunity(communityId, userDetails);
        return ResponseEntity.ok("You subscribed");
    }

    @Secured("ROLE_USER")
    @PutMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeToCommunity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestParam Long communityId){
        communityService.unsubscribeFromCommunity(communityId, userDetails);
        return ResponseEntity.ok("You unsubscribed");
    }

    @Secured("ROLE_USER")
    @GetMapping("/community")
    public ResponseEntity<CommunityDto> getCommunity(@RequestParam Long communityId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommunityDto.
                        mapFromEntity(communityService.findById(communityId)));
    }

    @Secured("ROLE_USER")
    @GetMapping("/subscribes")
    public ResponseEntity<Page<CommunityDto>> usersSubscribes(@RequestParam Long userId,
                                                              @PageableDefault Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityService.findByUserId(userId, pageable)
                        .map(CommunityDto::mapFromEntitySimplified));
    }

}
