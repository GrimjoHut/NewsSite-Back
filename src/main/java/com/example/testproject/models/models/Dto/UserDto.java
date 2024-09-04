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
    private List<RoleEnum> roles;

    public static UserDtoBuilder basicMapping(User user){
        return UserDto.builder()
                .nickname(user.getNickname());
    }

    public static UserDto mapFromEntity(User user){
        return basicMapping(user).build();
    }
}
