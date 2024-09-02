package com.example.testproject.controllers;

import com.example.testproject.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        String bucketName = "image-bucket";
        String imageUrl = "http://localhost:9000/" + bucketName + "/" + fileName; // Получаем URL из базы данных

        Resource resource = imageService.loadImageAsResource(imageUrl);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // Укажите правильный тип контента
                .body(resource);
    }
}

