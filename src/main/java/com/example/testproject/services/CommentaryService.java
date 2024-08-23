package com.example.testproject.services;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.CommentaryDTO;
import com.example.testproject.models.models.PostDTO;
import com.example.testproject.repositories.CommentaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentaryService {
    private final CommentaryRepository commentaryRepository;

    public ResponseEntity getOneCommentaryToPost(Post post){
        if (post.getCommentaries().isEmpty()){
            return ResponseEntity.ok().body("There are no commentaries yet");
        }
        return ResponseEntity.ok().body(new CommentaryDTO(commentaryRepository.findTopByPostOrderByCreatedAtDesc(post)));
    }

    public ResponseEntity getTenCommentaryToPost(Post post, Integer offset){

        int pageSize = 10;

        Pageable pageable = PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<CommentaryDTO> CommentaryDTOList = commentaryRepository.findByPostOrderByCreatedAtDesc(pageable, post).stream().map(CommentaryDTO::new).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(CommentaryDTOList);
    }
}
