package com.example.testproject.services;
import io.minio.http.Method;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
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
public class ImageService {

    @Autowired
    private MinioClient minioClient;

    public String uploadImage(MultipartFile file, String bucketName) throws Exception {
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
        } catch (MinioException e) {
            throw new Exception("Error uploading file to MinIO: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new Exception("Error reading the file: " + e.getMessage(), e);
        }

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .method(Method.GET) // Используйте GET для доступа к изображению
                        .expiry(60 * 60 * 24) // 24 часа
                        .build()
        );
    }

    public Resource loadImageAsResource(String imageUrl) {
        try {
            UrlResource resource = new UrlResource(imageUrl);
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file from URL!");
            }
        } catch (MalformedURLException e) {
            System.out.println("URL is malformed: " + e.getMessage());
            throw new RuntimeException("URL is malformed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading resource: " + e.getMessage());
            throw new RuntimeException("Error loading resource: " + e.getMessage());
        }
    }
}