package com.example.testproject.services;

import com.example.testproject.models.models.PostDTO;
import com.example.testproject.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public ResponseEntity getFivePosts(int offsetMultiplier){
        int pageSize = 5;

        Pageable pageable = PageRequest.of(offsetMultiplier, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<PostDTO> postDTOList = postRepository.findAllByOrderByCreatedAtDesc(pageable).stream().map(PostDTO::new).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(postDTOList);
    }

    public ResponseEntity getPost(int id){
        return ResponseEntity.status(HttpStatus.OK).body(postRepository.findById(id));
    }
}
