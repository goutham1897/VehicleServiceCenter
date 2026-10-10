package com.example.vehicleservice.repository;

import com.example.vehicleservice.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository
        extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(
            String registrationNumber
    );

    boolean existsByRegistrationNumber(
            String registrationNumber
    );

    List<Vehicle> findByCustomerId(Long customerId);

    Page<Vehicle> findByCustomerId(
            Long customerId,
            Pageable pageable
    );
}