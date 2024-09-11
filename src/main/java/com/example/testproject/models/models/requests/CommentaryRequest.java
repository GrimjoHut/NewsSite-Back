package com.example.testproject.models.models.requests;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.enums.CommentPermissionEnum;
import lombok.Data;

@Data
public class CommentaryRequest {
    private String description;
    private Long postId;
    private Long commId;


    public Commentary mapToEntity(CommentaryRequest commentaryRequest){
        Commentary commentary = new Commentary();
        commentary.setDescription(commentaryRequest.getDescription());
        return commentary;
    }
}
