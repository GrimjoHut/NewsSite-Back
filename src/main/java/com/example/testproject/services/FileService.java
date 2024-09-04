package com.example.testproject.services;

import com.example.testproject.models.entities.File;
import com.example.testproject.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public File createFile(String fileName, String bucketName, MultipartFile file) {
        File newFile = new File();
        newFile.setName(fileName);
        newFile.setBucket(bucketName);
        newFile.setPath("/" + bucketName + "/" + fileName);
        newFile.setSize(file.getSize());
        newFile.setFileExtension(file.getContentType());
        return fileRepository.save(newFile);
    }
}

