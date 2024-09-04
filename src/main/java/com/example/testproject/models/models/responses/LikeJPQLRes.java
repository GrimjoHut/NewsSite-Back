package com.example.testproject.models.models.responses;

import lombok.Builder;
import lombok.Data;

@Data
public class LikeJPQLRes {

    private Long likes;
    private Long dislikes;

    public LikeJPQLRes(Long likes, Long dislikes){
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
