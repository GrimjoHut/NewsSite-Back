package com.example.testproject.models.models.Post;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.UserDTO;
import com.example.testproject.utils.DateCalculator;
import lombok.Data;

import java.util.List;

@Data
public class ShortPostDTO {
    private Integer likes;
    private Integer dislikes;
    private String header;
    private String description;
    private UserDTO author; // Обновлено
    private String createDate;
    private List<String> imageUrls; // Убедитесь, что это поле присутствует

    public ShortPostDTO(Post post){
        this.likes = post.getLikes().size();
        this.dislikes = post.getDislikes().size();
        this.author = new UserDTO(post.getUser()); // Используем UserDTO
        this.header = post.getHeader();
        this.description = post.getDescription().length() < 400 ? post.getDescription() : post.getDescription().substring(0, 250) + "...";
        this.createDate = DateCalculator.formatDate(post.getCreatedAt());
        this.imageUrls = post.getImageUrls(); // Убедитесь, что это поле заполнено
    }
}
