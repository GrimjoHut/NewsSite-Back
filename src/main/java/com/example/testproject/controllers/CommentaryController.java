package com.example.testproject.controllers;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.CommentaryDTO;
import com.example.testproject.services.CommentaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/commentaries{id}")
    public ResponseEntity<CommentaryDTO> getComment(@PathVariable Integer id){
        return commentaryService.getOneCommentaryToPost(id);
    }

    @GetMapping("/commentariesToPost{id}")
    public ResponseEntity<List<CommentaryDTO>> getAllComments(@PathVariable Integer id, @RequestParam Integer offset){
        return commentaryService.getTenCommentaryToPost(id, offset);
    }
}
