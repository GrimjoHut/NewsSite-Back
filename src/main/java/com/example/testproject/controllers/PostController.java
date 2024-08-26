package com.example.testproject.controllers;

import com.example.testproject.models.models.Post.PostWithCommentDTO;
import com.example.testproject.models.models.Post.ShortPostDTO;
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
    public ResponseEntity<List<PostWithCommentDTO>> getPosts(@RequestParam Integer offset) {
        return postService.getFivePosts(offset);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<ShortPostDTO> getPost(@PathVariable Integer id) {
        return postService.getPost(id);
    }

    @PostMapping("/new_post")
    public ResponseEntity<?> createPost(@RequestBody RequestDTO requestDTO) {
        return postService.createPost(requestDTO);
    }

    @DeleteMapping("/delete_post")
    public ResponseEntity<?> deletePost(@RequestParam Integer id) {
        return postService.deletePost(id);
    }

    @PutMapping("/like_post")
    public ResponseEntity<?> likePost(@RequestParam Integer userId, @RequestParam Integer postId) {
        return postService.likePost(userId, postId);
    }

    @PutMapping("/dislike_post")
    public ResponseEntity<?> dislikePost(@RequestParam Integer userId, @RequestParam Integer postId) {
        return postService.dislikePost(userId, postId);
    }
}
