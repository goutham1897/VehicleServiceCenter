package com.example.vehicleservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.vehicleservice.dto.PartRequestDto;
import com.example.vehicleservice.dto.PartResponseDto;

public interface PartService {

    PartResponseDto createPart(
            PartRequestDto requestDto);

    PartResponseDto getPartById(
            Long id);

    Page<PartResponseDto> getAllParts(
            Pageable pageable);

    Page<PartResponseDto> getAvailableParts(
            Pageable pageable);

    PartResponseDto updatePart(
            Long id,
            PartRequestDto requestDto);

    void deletePart(Long id);

    void reduceStock(
            Long partId,
            Integer quantity);
}