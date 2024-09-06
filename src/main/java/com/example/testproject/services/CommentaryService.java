package com.example.testproject.services;

import com.example.testproject.exceptions.custom.CommentaryNotFoundException;
import com.example.testproject.exceptions.custom.PermissionNotAllowed;
import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.Dto.CommentaryDto;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.repositories.CommentaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    private final PostService postService;
    private final UserService userService;

    public Commentary findById(Long id) {
        return commentaryRepository.findById(id).orElseThrow(CommentaryNotFoundException::new);
    }

    public Page<Commentary> getTenCommentaties(Pageable pageable, Long post_id) {
        Post post = postService.findById(post_id);
        Page<Commentary> commentaries = commentaryRepository
                .findByPostOrderByCreatedDateDesc(pageable, post);
        return commentaries;
    }

    public void createComment(CommentaryDto commentaryDTO, Long user_id, Long post_id) {
        Commentary commentary = new Commentary();
        commentary.setCreatedDate(OffsetDateTime.now());
        commentary.setPost(postService.findById(post_id));
        commentary.setDescription(commentaryDTO.getDescription());
        commentary.setUser(userService.findById(user_id));
        commentaryRepository.save(commentary);
    }

    public void deleteComment(Long commentId, Long userId) {
        Commentary commentary = this.findById(commentId);
        User user = userService.findById(userId);
        if (commentary.getUser().getId().equals(userId) || user.getRoles().contains(RoleEnum.MODER)) {
            commentaryRepository.deleteById(commentId);
        } else throw new RuntimeException("Permission denied");
    }

    public void changeComment(Long comment_id, Long user_id, String text) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Commentary commentary = this.findById(comment_id);
        User user = userService.findById(user_id);
        if (commentary.getUser().getId() == user_id) {
            commentary.setDescription(text);
            commentary.setCreatedDate(OffsetDateTime.now());
            commentaryRepository.save(commentary);
        } else throw new PermissionNotAllowed();
    }
}
