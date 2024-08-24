package com.example.testproject.models.models.Post;

import com.example.testproject.models.entities.Post;
import com.example.testproject.utils.DateCalculator;
import lombok.Data;

@Data
public class ShortPostDTO {
    private Integer likes;
    private Integer dislikes;
    private String header;
    private String description;
    private String author;
    private String createDate;

    public ShortPostDTO(Post post){
        this.likes = post.getLikes().size();
        this.dislikes = post.getDislikes().size();
        this.author = post.getUser().getNickname();
        this.header = post.getHeader();
        if (post.getDescription().length() < 400) this.description = post.getDescription();
        else  this.description = post.getDescription().substring(0, 250) + "...";
        this.createDate = DateCalculator.formatDate(post.getCreatedAt());
    }
}
