package com.example.vehicleservice.serviceimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.vehicleservice.exception.ResourceNotFoundException;
import com.example.vehicleservice.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    private final Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

    public FileServiceImpl() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a file to upload");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.isBlank()
                || fileName.contains("..")
                || fileName.contains("/")
                || fileName.contains("\\")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        try {
            Path targetLocation = uploadDir.resolve(fileName).normalize();

            if (!targetLocation.startsWith(uploadDir)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + fileName, e);
        }
    }

    @Override
    public Resource downloadFile(String fileName) {

        if (fileName == null || fileName.isBlank()
                || fileName.contains("..")
                || fileName.contains("/")
                || fileName.contains("\\")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        try {
            Path filePath = uploadDir.resolve(fileName).normalize();

            if (!filePath.startsWith(uploadDir) || !Files.isRegularFile(filePath)) {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }

            throw new ResourceNotFoundException("File is not readable: " + fileName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to download file: " + fileName, e);
        }
    }
}