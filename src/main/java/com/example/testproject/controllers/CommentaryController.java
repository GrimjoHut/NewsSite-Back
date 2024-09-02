package com.example.testproject.controllers;

import com.example.testproject.models.DTO.CommentaryDTO;
import com.example.testproject.services.CommentaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/commentariesToPost/{id}")
    public ResponseEntity<List<CommentaryDTO>> getAllComments(@PathVariable Integer id, @RequestParam Integer offset){
        return commentaryService.getTenCommentaryToPost(id, offset);
    }

    @PostMapping("/createComment")
    public ResponseEntity createComment(@RequestBody CommentaryDTO commentaryDTO, @RequestParam Integer post_id, @RequestParam Integer user_id){
        return commentaryService.createComment(commentaryDTO, user_id, post_id);
    }

    @PutMapping("/changeComment/{id}")
    public ResponseEntity changeComment(@RequestParam String text, @RequestParam Integer comment_id, @RequestParam Integer user_id){
        return commentaryService.changeComment(comment_id, user_id, text);
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity deleteComment(@RequestParam Integer id, @RequestParam Integer userId){
        return commentaryService.deleteComment(id, userId);
    }
}
