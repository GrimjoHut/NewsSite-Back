package com.example.testproject.services;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.models.CommentaryDTO;
import com.example.testproject.repositories.CommentaryRepository;
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
public class CommentaryService {
    private final CommentaryRepository commentaryRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity<List<CommentaryDTO>> getTenCommentaryToPost(Integer id, Integer offset){

        int pageSize = 10;

        Post post = postRepository.findById(id).get();

        Pageable pageable = PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<CommentaryDTO> CommentaryDTOList = commentaryRepository
                .findByPostOrderByCreatedAtDesc(pageable, post)
                .stream()
                .map(CommentaryDTO::new)
                .collect(Collectors
                        .toList());

        return ResponseEntity.status(HttpStatus.OK).body(CommentaryDTOList);
    }

    public ResponseEntity createComment(CommentaryDTO commentaryDTO, Integer user_id, Integer post_id){
        LocalDateTime localDateTime = LocalDateTime.now();
        Commentary commentary = new Commentary();
        commentary.setCreatedAt(localDateTime);
        commentary.setPost(postRepository.findById(post_id).get());
        commentary.setDescription(commentaryDTO.getText());
        commentary.setUser(userRepository.findById(user_id).get());
        commentaryRepository.save(commentary);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteComment(Integer id){
        commentaryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity changeComment(Integer id, String text){
        LocalDateTime localDateTime = LocalDateTime.now();
        Commentary commentary = commentaryRepository.findById(id).get();
        commentary.setDescription(text);
        commentary.setCreatedAt(localDateTime);
        commentaryRepository.save(commentary);
        return ResponseEntity.ok().build();
    }
}
