package com.example.testproject.models.models.Post;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.CommentaryDTO;
import com.example.testproject.utils.DateCalculator;
import lombok.Data;

import java.util.List;


@Data
public class PostWithCommentDTO {
    private Integer likes;
    private Integer dislikes;
    private String header;
    private String description;
    private CommentaryDTO commentaryDTO;
    private String author;
    private String createDate;
    private List<String> imageUrls;

    public PostWithCommentDTO(Post post, CommentaryDTO commentaryDTO){
        this.likes = post.getLikes().size();
        this.dislikes = post.getDislikes().size();
        this.author = post.getUser().getNickname();
        this.header = post.getHeader();
        this.description = post.getDescription();
        this.commentaryDTO = commentaryDTO;
        this.createDate = DateCalculator.formatDate(post.getCreatedAt());
    }
}
