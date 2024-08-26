package com.example.testproject.configs;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://localhost:9000") // URL вашего MinIO сервера
                .credentials("admin", "password123") // ваши креденшелы
                .build();
    }
}
