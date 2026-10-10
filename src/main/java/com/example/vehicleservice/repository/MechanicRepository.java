package com.example.vehicleservice.repository;

import com.example.vehicleservice.entity.Mechanic;
import com.example.vehicleservice.enums.MechanicStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MechanicRepository
        extends JpaRepository<Mechanic, Long> {

    Optional<Mechanic> findByEmail(String email);

    Optional<Mechanic> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<Mechanic> findByStatus(MechanicStatus status);

    Page<Mechanic> findByStatus(
            MechanicStatus status,
            Pageable pageable
    );
}