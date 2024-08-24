package com.example.testproject.controllers;


import com.example.testproject.models.models.Post.PostWithCommentDTO;
import com.example.testproject.models.models.Post.PostDTO;
import com.example.testproject.models.models.RequestDTO;
import com.example.testproject.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<PostWithCommentDTO>> getPosts(@RequestParam Integer offset){
        return postService.getFivePosts(offset);
    }

    @GetMapping("/post")
    public ResponseEntity<PostDTO> getPost(@RequestParam Integer id){
        return postService.getPost(id);
    }

    @PostMapping("/new_post")
    public ResponseEntity createPost(@RequestBody RequestDTO requestDTO){
        return postService.createPost(requestDTO);
    }
}
