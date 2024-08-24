package com.example.testproject.models.models.Post;

import com.example.testproject.models.entities.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Integer likes;
    private Integer dislikes;
    private String header;
    private String description;
    private String author;
    private LocalDateTime createDate;

    public PostDTO(Post post){
        this.likes = post.getLikes().size();
        this.dislikes = post.getDislikes().size();
        this.author = post.getUser().getNickname();
        this.header = post.getHeader();
        this.description = post.getDescription();
        this.createDate = post.getCreatedAt();
    }
}
