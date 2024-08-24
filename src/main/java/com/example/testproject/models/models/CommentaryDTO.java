package com.example.testproject.models.models;

import com.example.testproject.models.entities.Commentary;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentaryDTO {
    private String text;
    private String author;
    private LocalDateTime createdAt;


    public CommentaryDTO(Commentary commentary){
        this.text = commentary.getDescription();
        this.author = commentary.getUser().getNickname();
        this.createdAt = commentary.getCreatedAt();
    }
}
