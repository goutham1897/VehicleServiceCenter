package com.example.vehicleservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.vehicleservice.entity.AppUser;
import com.example.vehicleservice.repository.AppUserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeAdminUser(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String username = "admin";

            if (!appUserRepository
                    .existsByUsername(username)) {

                AppUser admin = new AppUser();

                admin.setUsername(username);

                admin.setPassword(
                    passwordEncoder.encode("admin123")
                );

                admin.setRole("ADMIN");

                appUserRepository.save(admin);

                System.out.println(
                    "Default admin user created successfully"
                );
            }
        };
    }
}