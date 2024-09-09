package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.custom.CommentaryNotFoundException;
import com.example.testproject.exceptions.custom.PermissionNotAllowed;
import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.models.models.Dto.CommentaryDto;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.models.models.requests.CommentaryRequest;
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

    public Page<Commentary> getCommentaries(Pageable pageable, Long post_id) {
        Post post = postService.findById(post_id);
        Page<Commentary> commentaries = commentaryRepository
                .findByPostOrderByCreatedDateDesc(pageable, post);
        return commentaries;
    }

    public Commentary createComment(CommentaryRequest commentaryRequest, Long post_id, CustomUserDetails userDetails) {
        Commentary commentary = new Commentary();
        commentary.setCreatedDate(OffsetDateTime.now());
        commentary.setPost(postService.findById(post_id));
        commentary.setDescription(commentaryRequest.getDescription());
        commentary.setUser(userDetails.getUser());
        commentaryRepository.save(commentary);
        return commentary;
    }

    public void deleteComment(Long commentId, CustomUserDetails userDetails) {
        Commentary commentary = this.findById(commentId);
        Long userId = userDetails.getUser().getId();
        if (commentary.getUser().getId().equals(userId)
                || userDetails.getAuthorities().contains("ROLE_MODER")) {
            commentaryRepository.deleteById(commentId);
        } else throw new PermissionNotAllowed();
    }

    public void changeComment(Long comment_id, CustomUserDetails userDetails, String text) {
        Commentary commentary = this.findById(comment_id);
        Long userId = userDetails.getUser().getId();
        if (commentary.getUser().getId().equals(userId)) {
            commentary.setDescription(text);
            commentary.setCreatedDate(OffsetDateTime.now());
            commentaryRepository.save(commentary);
        } else throw new PermissionNotAllowed();
    }
}
