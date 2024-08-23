package com.example.testproject.controllers;

import com.example.testproject.models.entities.Post;
import com.example.testproject.services.CommentaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/commentaries")
    public ResponseEntity getComments(Post post){
        return commentaryService.getOneCommentaryToPost(post);
    }

    @GetMapping("/commentariesToPost")
    public ResponseEntity getAllComments(){

    }
}
