package com.example.vehicleservice.repository;

import com.example.vehicleservice.entity.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartRepository
        extends JpaRepository<Part, Long> {

    Optional<Part> findByPartNumber(String partNumber);

    boolean existsByPartNumber(String partNumber);

    Page<Part> findByAvailableQuantityGreaterThan(
            Integer quantity,
            Pageable pageable
    );
}