package com.example.testproject.services;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.PostDTO;
import com.example.testproject.models.models.RequestDTO;
import com.example.testproject.repositories.PostRepository;
import com.example.testproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity getFivePosts(int offsetMultiplier){
        int pageSize = 10;

        Pageable pageable = PageRequest.of(offsetMultiplier, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<PostDTO> postDTOList = postRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors
                        .toList());

        return ResponseEntity.status(HttpStatus.OK).body(postDTOList);
    }

    public ResponseEntity getPost(int id){
        return ResponseEntity.status(HttpStatus.OK).body(new PostDTO(postRepository.findById(id)));
    }

    public ResponseEntity createPost(RequestDTO requestDTO){
        LocalDateTime localDateTime = LocalDateTime.now();
        Post post = new Post();
        post.setHeader(requestDTO.getHeader());
        post.setDescription(requestDTO.getDescription());
        post.setUser(userRepository.findById(requestDTO.getUser().getId()).get());
        post.setCreatedAt(localDateTime);
        postRepository.save(post);
        return ResponseEntity.ok().build();
    }
}
