package com.example.testproject.controllers;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.Dto.PostDto;
import com.example.testproject.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<Page<PostDto>> getPosts(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService
                        .getFivePosts(pageable)
                        .map(PostDto::mapFromEntitySimplified));
    }

    @GetMapping("/post")
    public ResponseEntity<?> getPost(@RequestParam Long id) {
        Post post = postService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostDto.mapFromEntity(post));
    }



    @PostMapping(value = "/newRequest", consumes = {"multipart/form-data"})
    public ResponseEntity<String> createRequest(
            @RequestPart("request") PostDto postDto,
            @RequestPart("files") List<MultipartFile> files){
        postService.createPost(postDto, files);
        return ResponseEntity.ok("Post created");
    }

    @DeleteMapping("/post")
    public ResponseEntity<?> deletePost(@RequestParam Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted");
    }
}
