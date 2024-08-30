package com.example.testproject.models.DTO;

import com.example.testproject.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String nickname;;

    public UserDTO(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
    }
}
