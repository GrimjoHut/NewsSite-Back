package com.example.testproject.services;

import com.example.testproject.models.models.Dto.PostDto;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.User;
import com.example.testproject.repositories.PostRepository;
import com.example.testproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }


    public Page<Post> getFivePosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedDateDesc(pageable);
       return posts;
    }


//    public ResponseEntity<Post> getPost(Long id){
//        return this.findById(id);
//    }

//    public void createPost(PostDto postDto) {
//        Post post = new Post();
//        post.setUser(userService.findByNickName(postDto.getAuthor().getNickname()));
//        post.setHeader(postDto.getHeader());
//        post.setDescription(postDto.getDescription());
//        post.setCreatedDate(OffsetDateTime.now());
//        List<String> imageUrls = new ArrayList<>();
//        if (postDto.getImages() != null) {
//            imageUrls.addAll(postDto.getImages().);
//        }
//        post.setImages(imageUrls);
//        postRepository.save(post);
//    }



//    public void deletePost(Long id){
//
//    }
}
