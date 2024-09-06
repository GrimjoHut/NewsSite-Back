package com.example.testproject.services;
import com.example.testproject.models.entities.File;
import com.example.testproject.models.entities.Image;
import com.example.testproject.models.entities.Post;
import com.example.testproject.repositories.ImageRepository;
import io.minio.http.Method;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final MinioClient minioClient;
    private final FileService fileService;
    private final ImageRepository imageRepository;


    public Image uploadImage(MultipartFile file, String bucketName) throws Exception {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (MinioException | IOException e) {
            throw new Exception("Ошибка загрузки файла в MinIO: " + e.getMessage(), e);
        }

        // Создаем и сохраняем сущность файла
        File savedFile = fileService.createFile(fileName, bucketName, file);

        // Создаем и сохраняем сущность изображения
        Image image = new Image();
        image.setFile(savedFile);
        return imageRepository.save(image);  // Возвращаем сохраненное изображение
    }

    public void deleteImage(Image image){
        imageRepository.delete(image);
    }
}
