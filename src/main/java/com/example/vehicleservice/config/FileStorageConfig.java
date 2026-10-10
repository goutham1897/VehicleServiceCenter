package com.example.vehicleservice.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Bean
    public Path fileStorageLocation() {
        return Paths.get("uploads")
                .toAbsolutePath()
                .normalize();
    }
}