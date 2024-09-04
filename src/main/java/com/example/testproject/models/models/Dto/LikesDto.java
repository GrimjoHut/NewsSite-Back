package com.example.testproject.models.models.Dto;

import com.example.testproject.models.entities.Likes;
import com.example.testproject.models.enums.LikesEnum;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class LikesDto {
    private UserDto author;
    private LikesEnum reaction;

    public static LikesDtoBuilder basicMapping(Likes likes){
        return LikesDto.builder()
                .author(UserDto.mapFromEntity(likes.getUser()))
                .reaction(likes.getReaction());
    }

    public static LikesDto mapFromEntity(Likes likes){
        return basicMapping(likes).build();
    }
}
