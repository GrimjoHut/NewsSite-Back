package com.example.testproject.controllers;

import com.example.testproject.models.models.Dto.LikesDto;
import com.example.testproject.models.entities.Likes;
import com.example.testproject.models.enums.LikesEnum;
import com.example.testproject.models.models.responses.LikeJPQLRes;
import com.example.testproject.services.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/count-likes")
    public ResponseEntity<LikeJPQLRes> getLikesToPost(@RequestParam(required = false) Long postId,
                                                      @RequestParam(required = false) Long commId)
    {
        return ResponseEntity.ok().body(likesService.likesPost(postId, commId));
    }

    @PostMapping("/likePost")
    public ResponseEntity<String> like(@RequestParam(required = false) Long postId,
                                       @RequestParam(required = false) Long commentId,
                                       @RequestParam LikesEnum reaction,
                                       @RequestParam Long userId) {
        likesService.like(postId, commentId, reaction, userId);
        return ResponseEntity.ok("Like/dislike recorded successfully");
    }

}
