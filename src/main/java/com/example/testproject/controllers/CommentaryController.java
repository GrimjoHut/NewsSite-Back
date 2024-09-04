package com.example.testproject.controllers;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.models.Dto.CommentaryDto;
import com.example.testproject.services.CommentaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/commentariesToPost/{id}")
    public ResponseEntity<Page<CommentaryDto>> getTenComments(@PageableDefault Pageable pageable,
                                                              @RequestParam Long post_id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentaryService
                        .getTenCommentaties(pageable, post_id)
                        .map(CommentaryDto::mapFromEntity));
    }

    @PostMapping("/createComment")
    public ResponseEntity<String> createComment(@RequestBody CommentaryDto commentaryDTO,
                                        @RequestParam Long post_id,
                                        @RequestParam Long user_id){
        commentaryService.createComment(commentaryDTO, user_id, post_id);
        return ResponseEntity.ok("Comment created");
    }
    @PutMapping("/changeComment/{id}")
    public ResponseEntity<String> changeComment(@RequestParam String text,
                                        @RequestParam Long comment_id,
                                        @RequestParam Long user_id){
        commentaryService.changeComment(comment_id, user_id, text);
        return ResponseEntity.ok("Comment changed");
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity deleteComment(@RequestParam Long postId, @RequestParam Long userId){
        commentaryService.deleteComment(postId, userId);
        return ResponseEntity.ok("Commentary deleted");
    }
}
