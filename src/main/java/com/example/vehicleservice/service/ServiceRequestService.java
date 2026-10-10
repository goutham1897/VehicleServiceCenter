package com.example.vehicleservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.vehicleservice.dto.ServiceRequestDto;
import com.example.vehicleservice.dto.ServiceResponseDto;
import com.example.vehicleservice.enums.ServiceStatus;

public interface ServiceRequestService {

    ServiceResponseDto createServiceRequest(
            ServiceRequestDto requestDto);

    ServiceResponseDto getServiceRequestById(
            Long id);

    Page<ServiceResponseDto> getAllServiceRequests(
            Pageable pageable);

    Page<ServiceResponseDto> getServiceRequestsByStatus(
            ServiceStatus status,
            Pageable pageable);

    Page<ServiceResponseDto> getServiceHistoryByVehicle(
            Long vehicleId,
            Pageable pageable);

    List<ServiceResponseDto> getServiceHistoryByCustomer(
            Long customerId);

    List<ServiceResponseDto> getJobsByMechanic(
            Long mechanicId);

    ServiceResponseDto updateServiceRequest(
            Long id,
            ServiceRequestDto requestDto);

    ServiceResponseDto assignMechanic(
            Long serviceRequestId,
            Long mechanicId);

    ServiceResponseDto updateJobStatus(
            Long serviceRequestId,
            ServiceStatus newStatus);

    ServiceResponseDto cancelServiceRequest(
            Long serviceRequestId);
}