package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.GeneralException;
import com.example.testproject.exceptions.custom.CommentaryNotFoundException;
import com.example.testproject.exceptions.custom.PermissionNotAllowedException;
import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.enums.StatusEnum;
import com.example.testproject.models.models.requests.CommentaryRequest;
import com.example.testproject.repositories.CommentaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    private final PostService postService;
    private final UserSecurityService userSecurityService;

    public Commentary findById(Long id) {
        return commentaryRepository.findById(id).orElseThrow(CommentaryNotFoundException::new);
    }


    public Page<Commentary> getCommentaries(Specification<Commentary> spec, Pageable pageable) {
        Page<Commentary> commentaries = commentaryRepository.findAll(spec, pageable);
        return commentaries;
    }

    public Boolean deleteCommentaryPermission(CustomUserDetails userDetails, Long commId){
        Commentary commentary = this.findById(commId);
        if (commentary.getPost().getCommunity() != null){
            if (userSecurityService.isCommunityAdmin(userDetails, (commentary.getPost().getCommunity().getId())))
                return true;
            else return commentary.getUser().equals(userDetails.getUser());
        }
        if (commentary.getPost().getUserBoard() != null){
            if (commentary.getPost().getUserBoard().getUser().equals(userDetails.getUser()))
                return true;
            else return commentary.getUser().equals(userDetails.getUser());
        }
        return false;
    }

    public Commentary createComment(CommentaryRequest commentaryRequest, CustomUserDetails userDetails) {
        if ((commentaryRequest.getPostId() != null && commentaryRequest.getCommId() != null)
            || (commentaryRequest.getPostId() == null && commentaryRequest.getCommId() == null))
            throw new GeneralException(400, "Wrong params");
        Commentary commentary = commentaryRequest.mapToEntity(commentaryRequest);
        if (commentaryRequest.getPostId() != null){
            if (postService.commentPermission(userDetails, commentaryRequest.getPostId())) {
                commentary.setPost(postService.findById(commentaryRequest.getPostId()));
            }
        }
        if (commentaryRequest.getCommId() != null){
            Commentary parentCommentary = this.findById(commentaryRequest.getCommId());
            if (postService.commentPermission(userDetails, parentCommentary.getId())) {
                commentary.setParentCommentary(this.findById(commentaryRequest.getCommId()));
            } else throw new PermissionNotAllowedException();
        }
        commentary.setUser(userDetails.getUser());
        commentaryRepository.save(commentary);
        return commentary;
    }

    public Commentary deleteComment(Long commentId, CustomUserDetails userDetails) {
        if (deleteCommentaryPermission(userDetails, commentId)) {
            Commentary commentary = this.findById(commentId);
            commentary.setStatus(StatusEnum.DELETED);
                commentaryRepository.save(commentary);
                return commentary;
        } else throw new PermissionNotAllowedException();
    }

    public Commentary changeComment(Long comment_id, CustomUserDetails userDetails, String text) {
        Commentary commentary = this.findById(comment_id);
        Long userId = userDetails.getUser().getId();
        if (commentary.getUser().getId().equals(userId)) {
            commentary.setDescription(text);
            commentary.setCreatedDate(OffsetDateTime.now());
            commentaryRepository.save(commentary);
            return commentary;
        } else throw new PermissionNotAllowedException();
    }
}
