package com.example.testproject.models.models;

import com.example.testproject.models.entities.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RequestDTO {
    private String header;
    private String description;
    private UserDTO user;
    private LocalDateTime createdAt;
    private List<String> imageUrls;  // Новое поле для хранения URL изображений

    public RequestDTO(Request request) {
        this.user = new UserDTO(request.getUser());
        this.description = request.getDescription();
        this.header = request.getHeader();
        this.createdAt = request.getCreatedAt();
        this.imageUrls = request.getImageUrls();  // Извлечение URL-ов из сущности
    }
}

