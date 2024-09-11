package com.example.testproject.models.models.requests;

import com.example.testproject.models.entities.User;
import lombok.Data;

@Data
public class UserRequest {
    private String nickname;
    private String password;
    private String email;

    public static User mapFromEntity(UserRequest userRequest){
        User user = new User();
        user.setPassword(userRequest.password);
        user.setNickname(userRequest.nickname);
        user.setEmail(userRequest.email);
        return user;
    }
}
