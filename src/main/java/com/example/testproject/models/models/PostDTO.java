package com.example.testproject.models.models;

import com.example.testproject.models.entities.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDTO {
    private Integer likes;
    private Integer dislikes;
    private String header;
    private String description;
    private List<CommentaryDTO> commentaries;
    private String author;
    private LocalDateTime createDate;

    public PostDTO(Post post){
        this.likes = post.getLikes().size();
        this.dislikes = post.getDislikes().size();
        this.author = post.getUser().getNickname();
        this.header = post.getHeader();
        this.description = post.getDescription();
        this.commentaries = post.getCommentaries().stream().map(CommentaryDTO::new).collect(Collectors.toList());
        this.createDate = post.getCreatedAt();
    }
}
