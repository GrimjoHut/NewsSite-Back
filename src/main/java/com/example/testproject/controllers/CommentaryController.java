package com.example.testproject.controllers;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.models.Dto.CommentaryDto;
import com.example.testproject.models.models.requests.CommentaryRequest;
import com.example.testproject.services.CommentaryService;
import com.example.testproject.specifications.CommentarySpecification;
import jakarta.persistence.Lob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/commentariesToPost/{id}")
    public ResponseEntity<Page<CommentaryDto>> getCommentaries(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long commId){
        Specification<Commentary> spec = Specification.where(
                CommentarySpecification.toPost(postId)
                .and(CommentarySpecification.toCommentary(commId)));
        Page<Commentary> commentaries = commentaryService.getCommentaries(spec, pageable);
        Page<CommentaryDto> commentaryDtos = commentaries.map(CommentaryDto::mapFromEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentaryDtos);
    }

    @Secured("ROLE_USER")
    @PostMapping("/createComment")
    public ResponseEntity<CommentaryDto> createComment(@RequestBody CommentaryRequest commentaryRequest,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        Commentary commentary = commentaryService.createComment(commentaryRequest, userDetails);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommentaryDto.mapFromEntity(commentary));
    }

    @Secured("ROLE_USER")
    @PutMapping("/changeComment/{id}")
    public ResponseEntity<CommentaryDto> changeComment(@RequestParam String text,
                                        @RequestParam Long comment_id,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        Commentary commentary = commentaryService.changeComment(comment_id, userDetails, text);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommentaryDto.mapFromEntity(commentary));
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/deleteComment")
    public ResponseEntity<CommentaryDto> deleteComment(@RequestParam Long commId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        Commentary commentary = commentaryService.deleteComment(commId, userDetails);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommentaryDto.mapFromEntity(commentary));
    }
}
