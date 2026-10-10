package com.example.vehicleservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.vehicleservice.dto.MechanicRequestDto;
import com.example.vehicleservice.dto.MechanicResponseDto;
import com.example.vehicleservice.enums.MechanicStatus;

public interface MechanicService {

    MechanicResponseDto createMechanic(
            MechanicRequestDto requestDto);

    MechanicResponseDto getMechanicById(
            Long id);

    Page<MechanicResponseDto> getAllMechanics(
            Pageable pageable);

    Page<MechanicResponseDto> getMechanicsByStatus(
            MechanicStatus status,
            Pageable pageable);

    MechanicResponseDto updateMechanic(
            Long id,
            MechanicRequestDto requestDto);

    void deleteMechanic(Long id);

    Page<MechanicResponseDto> getAvailableMechanics(
            Pageable pageable);
}