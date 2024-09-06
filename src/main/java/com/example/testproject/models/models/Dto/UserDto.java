package com.example.testproject.models.models.Dto;

import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private String nickname;
    private String email;
    private ImageDto avatar;


    public static UserDtoBuilder basicMapping(User user){
        return UserDto.builder()
                .nickname(user.getNickname())
                .avatar(ImageDto.mapFromEntity(user.getAvatar()));
    }

    public static UserDto mapFromEntitySimplify (User user){
        return basicMapping(user).build();
    }

    public static UserDto mapFromEntity (User user){
        return basicMapping(user)
                .email(user.getEmail())
                .avatar(ImageDto.mapFromEntity(user.getAvatar()))
                .build();
    }
}
