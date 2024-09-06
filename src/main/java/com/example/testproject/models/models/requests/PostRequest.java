package com.example.testproject.models.models.requests;

import lombok.Data;

@Data
public class PostRequest {
    private String description;
    private Long communityId;
    private String header;
}
