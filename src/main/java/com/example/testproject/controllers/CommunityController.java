package com.example.testproject.controllers;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.models.Dto.CommunityDto;
import com.example.testproject.models.models.requests.CommunityRequest;
import com.example.testproject.services.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @Secured("ROLE_USER")
    @PostMapping(value = "/newPublic", consumes = {"multipart/form-data"})
    public ResponseEntity<CommunityDto> createCommunity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody CommunityRequest communityRequest,
                                                        @RequestPart MultipartFile file){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommunityDto
                        .mapFromEntity(communityService
                                .createCommunity(communityRequest, userDetails, file)));
    }

}
