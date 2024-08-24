package com.example.testproject.models.models.Post;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.CommentaryDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
public class PostWithCommentDTO {
    private Integer likes;
    private Integer dislikes;
    private String header;
    private String description;
    private CommentaryDTO commentaryDTO;
    private String author;
    private LocalDateTime createDate;

    public PostWithCommentDTO(Post post, CommentaryDTO commentaryDTO){
        this.likes = post.getLikes().size();
        this.dislikes = post.getDislikes().size();
        this.author = post.getUser().getNickname();
        this.header = post.getHeader();
        this.description = post.getDescription();
        this.commentaryDTO = commentaryDTO;
        this.createDate = post.getCreatedAt();
    }
}
