package com.example.testproject.models.models;

import com.example.testproject.models.entities.Request;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestDTO {
    private String header;
    private String description;
    private UserDTO user;
    private LocalDateTime createdAt;

    public RequestDTO(Request request){
        this.user = new UserDTO(request.getUser());
        this.description = request.getDescription();
        this.header = request.getHeader();
        this.createdAt = request.getCreatedAt();
    }
}
