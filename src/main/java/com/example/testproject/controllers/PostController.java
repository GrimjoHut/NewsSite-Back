package com.example.testproject.controllers;


import com.example.testproject.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity getPosts(@RequestParam Integer offset){
        return postService.getFivePosts(offset);
    }

    @GetMapping("/post")
    public ResponseEntity getPost(@RequestParam Integer id){
        return postService.getPost(id);
    }
}
