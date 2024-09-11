package com.example.testproject.controllers;

import com.example.testproject.Security.AdminInCommunity;
import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.entities.*;
import com.example.testproject.models.enums.CommentPermissionEnum;
import com.example.testproject.models.enums.FriendStatusEnum;
import com.example.testproject.models.enums.StatusEnum;
import com.example.testproject.models.models.Dto.PostDto;
import com.example.testproject.models.models.requests.PostRequest;
import com.example.testproject.services.PostService;
import com.example.testproject.services.UserSecurityService;
import com.example.testproject.specifications.PostSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserSecurityService userSecurityService;

    @GetMapping("/posts")
    public ResponseEntity<Page<PostDto>> getPosts(
            @RequestParam(required = false) Long userBoardId,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) StatusEnum status,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Boolean reported,
            @PageableDefault Pageable pageable){
        Specification<Post> spec = Specification.where(
                            PostSpecifications.inCommunity(communityId)
                        .and(PostSpecifications.inUserBoard(userBoardId)
                        .and(PostSpecifications.hasReports(reported))));
        if (communityId != null) {
            if (userSecurityService.isCommunityAdmin(userDetails, communityId)) {
                spec = spec.and(PostSpecifications.hasStatus(StatusEnum.PUBLISHED));
            } else {
                spec = spec.and(PostSpecifications.hasStatus(status));
            }
        } else spec = spec.and(PostSpecifications.hasStatus(StatusEnum.PUBLISHED));
        if (userDetails != null && communityId == null && userBoardId == null) {
            Set<Community> subscribedCommunities = userDetails.getUser().getSubscribes();
            List<Long> communityIds = subscribedCommunities.stream()
                    .map(Community::getId)
                    .collect(Collectors.toList());

            spec = spec.and(PostSpecifications.inCommunities(communityIds));

            List<Long> friendBoards = userDetails.getUser().getFriends().stream()
                    .filter(f -> f.getStatus() == FriendStatusEnum.FRIEND || f.getStatus() == FriendStatusEnum.REQUESTED)
                    .map(Friends::getFriend)
                    .map(User::getUserBoard)
                    .map(UserBoard::getId)
                    .collect(Collectors.toList());

            spec = spec.or(PostSpecifications.inUserBoards(friendBoards));
        }

        Page<Post> posts = postService.getPosts(spec, pageable);
        Page<PostDto> postDtos = posts.map(PostDto::mapFromEntitySimplified);
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/post")
    public ResponseEntity<PostDto> getPost(@RequestParam Long id) {
        Post post = postService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostDto.mapFromEntity(post));
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/newRequest", consumes = {"multipart/form-data"})
    public ResponseEntity<Post> createPost(
            @RequestPart("request") PostRequest postRequest,
            @RequestPart("files") List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.createPost(postRequest, files, userDetails));
    }

    @Secured("ROLE_USER")
    @PutMapping("/changeCommPermission")
    public ResponseEntity<PostDto> changeCommPermission(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestParam Long postId,
                                                        @RequestParam CommentPermissionEnum permission){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostDto.mapFromEntity(
                        postService.changeCommPermission(userDetails, postId, permission)
                ));
    }

    @Secured("ROLE_USER")
    @AdminInCommunity
    @PutMapping("/acceptCommunityPost")
    public ResponseEntity<Post> acceptCommunityPostRequest(@RequestParam Long postId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.acceptCommunityPost(postId));
    }

    @Secured("ROLE_USER")
    @AdminInCommunity
    @PutMapping("/declineCommunityPost")
    public ResponseEntity<PostDto> declineCommunityPostRequest(@RequestParam Long postId){
        postService.declineCommunityPost(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostDto.mapFromEntity(
                        postService.declineCommunityPost(postId)
                ));
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/post")
    public ResponseEntity<PostDto> deletePost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestParam Long postId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostDto.mapFromEntity(
                        postService.deletePost(userDetails, postId)));
    }
}
