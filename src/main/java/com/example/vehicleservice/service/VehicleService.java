package com.example.vehicleservice.service;

import com.example.vehicleservice.dto.VehicleRequestDto;
import com.example.vehicleservice.dto.VehicleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface VehicleService {

    VehicleResponseDto createVehicle(
            VehicleRequestDto requestDto
    );

    VehicleResponseDto getVehicleById(
            Long id
    );

    Page<VehicleResponseDto> getAllVehicles(
            Pageable pageable
    );

    Page<VehicleResponseDto> getVehiclesByCustomer(
            Long customerId,
            Pageable pageable
    );

    VehicleResponseDto updateVehicle(
            Long id,
            VehicleRequestDto requestDto
    );

    void deleteVehicle(
            Long id
    );

    VehicleResponseDto getVehicleByRegistrationNumber(
            String registrationNumber
    );
}