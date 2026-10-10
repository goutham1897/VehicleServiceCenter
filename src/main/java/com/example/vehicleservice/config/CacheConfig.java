package com.example.vehicleservice.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {

        CaffeineCacheManager cacheManager =
                new CaffeineCacheManager(
                    "customers",
                    "vehicles",
                    "mechanics",
                    "parts",
                    "serviceRequests",
                    "invoices"
                );

        cacheManager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(
                    Duration.ofMinutes(10)
                )
        );

        return cacheManager;
    }
}