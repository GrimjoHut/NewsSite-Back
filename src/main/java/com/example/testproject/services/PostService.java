package com.example.testproject.services;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.models.CommentaryDTO;
import com.example.testproject.models.models.Post.ShortPostDTO;
import com.example.testproject.models.models.Post.PostWithCommentDTO;
import com.example.testproject.models.models.RequestDTO;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentaryRepository commentaryRepository;

    public ResponseEntity<List<PostWithCommentDTO>> getFivePosts(int offsetMultiplier) {
        int pageSize = 5;

        Pageable pageable = PageRequest.of(offsetMultiplier, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<PostWithCommentDTO> postDTOList = posts.stream()
                .map(post -> {
                    Commentary latestComment = commentaryRepository.findTopByPostOrderByCreatedAtDesc(post);
                    return new PostWithCommentDTO(post, new CommentaryDTO(latestComment));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOList);
    }

    public ResponseEntity getPost(int id){
        return ResponseEntity.status(HttpStatus.OK).body(new ShortPostDTO(postRepository.findById(id)));
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

    public ResponseEntity deletePost(Integer id){
        postRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> likePost(Integer userId, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
            postRepository.save(post);
            return ResponseEntity.ok("Like removed");
        }

        if (post.getDislikes().contains(user)) {
            post.getDislikes().remove(user);
        }

        post.getLikes().add(user);
        postRepository.save(post);
        return ResponseEntity.ok("Like added");
    }

    public ResponseEntity<?> dislikePost(Integer userId, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (post.getDislikes().contains(user)) {
            post.getDislikes().remove(user);
            postRepository.save(post);
            return ResponseEntity.ok("Dislike removed");
        }

        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
        }

        post.getDislikes().add(user);
        postRepository.save(post);
        return ResponseEntity.ok("Dislike added");
    }
}
