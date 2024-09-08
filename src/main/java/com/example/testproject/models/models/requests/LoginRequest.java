package com.example.testproject.models.models.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String nickname;
    private String password;
    private String email;
}
