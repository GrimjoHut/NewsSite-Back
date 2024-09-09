package com.example.testproject.controllers;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.models.Dto.CommentaryDto;
import com.example.testproject.models.models.requests.CommentaryRequest;
import com.example.testproject.services.CommentaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/commentariesToPost/{id}")
    public ResponseEntity<Page<CommentaryDto>> getCommentaries(@PageableDefault Pageable pageable,
                                                              @RequestParam Long post_id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentaryService
                        .getCommentaries(pageable, post_id)
                        .map(CommentaryDto::mapFromEntity));
    }

    @Secured("ROLE_USER")
    @PostMapping("/createComment")
    public ResponseEntity<Commentary> createComment(@RequestBody CommentaryRequest commentaryRequest,
                                                    @RequestParam Long post_id,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentaryService
                        .createComment(commentaryRequest, post_id, userDetails));
    }

    @Secured("ROLE_USER")
    @PutMapping("/changeComment/{id}")
    public ResponseEntity<String> changeComment(@RequestParam String text,
                                        @RequestParam Long comment_id,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        commentaryService.changeComment(comment_id, userDetails, text);
        return ResponseEntity.ok("Comment changed");
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/deleteComment")
    public ResponseEntity deleteComment(@RequestParam Long commId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        commentaryService.deleteComment(commId, userDetails);
        return ResponseEntity.ok("Commentary deleted");
    }
}
