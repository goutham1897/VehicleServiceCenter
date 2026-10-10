package com.example.vehicleservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.vehicleservice.dto.ServiceRequestDto;
import com.example.vehicleservice.dto.ServiceResponseDto;
import com.example.vehicleservice.entity.Mechanic;
import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.enums.MechanicStatus;
import com.example.vehicleservice.enums.ServiceStatus;
import com.example.vehicleservice.enums.ServiceType;
import com.example.vehicleservice.mapper.VehicleServiceMapper;
import com.example.vehicleservice.repository.MechanicRepository;
import com.example.vehicleservice.repository.ServiceRequestRepository;
import com.example.vehicleservice.repository.VehicleRepository;
import com.example.vehicleservice.serviceimpl.ServiceRequestServiceImpl;

@ExtendWith(MockitoExtension.class)
class ServiceRequestServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private ServiceRequestServiceImpl serviceRequestService;

    private Vehicle vehicle;
    private Mechanic mechanic;
    private ServiceRequest serviceRequest;
    private ServiceRequestDto requestDto;

    @BeforeEach
    void setUp() {

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setRegistrationNumber("TN01AB1234");

        mechanic = new Mechanic();
        mechanic.setId(1L);
        mechanic.setName("Arun");
        mechanic.setStatus(MechanicStatus.AVAILABLE);

        serviceRequest = new ServiceRequest();
        serviceRequest.setId(1L);
        serviceRequest.setRequestNumber("SR-12345");
        serviceRequest.setVehicle(vehicle);
        serviceRequest.setMechanic(mechanic);
        serviceRequest.setServiceType(ServiceType.GENERAL_SERVICE);
        serviceRequest.setProblemDescription("General service required");
        serviceRequest.setStatus(ServiceStatus.REQUESTED);
        serviceRequest.setRequestDate(LocalDateTime.now());
        serviceRequest.setEstimatedCost(new BigDecimal("2500"));

        requestDto = new ServiceRequestDto();

        requestDto.setVehicleId(1L);
        requestDto.setMechanicId(1L);
        requestDto.setServiceType(ServiceType.GENERAL_SERVICE);
        requestDto.setProblemDescription("General service required");
        requestDto.setEstimatedCost(new BigDecimal("2500"));
    }

    @Test
    void createServiceRequest_success() {

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(mechanicRepository.findById(1L))
                .thenReturn(Optional.of(mechanic));

        when(serviceRequestRepository.save(any(ServiceRequest.class)))
                .thenReturn(serviceRequest);

        ServiceResponseDto responseDto = new ServiceResponseDto();

        when(mapper.toServiceResponseDto(serviceRequest))
                .thenReturn(responseDto);

        ServiceResponseDto result =
                serviceRequestService.createServiceRequest(requestDto);

        assertNotNull(result);

        verify(vehicleRepository).findById(1L);
        verify(mechanicRepository).findById(1L);
        verify(serviceRequestRepository).save(any(ServiceRequest.class));
        verify(mapper).toServiceResponseDto(serviceRequest);
    }

    @Test
    void createServiceRequest_vehicleNotFound() {

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> serviceRequestService
                        .createServiceRequest(requestDto)
        );

        verify(vehicleRepository).findById(1L);

        verify(serviceRequestRepository, never())
                .save(any(ServiceRequest.class));
    }

    @Test
    void createServiceRequest_mechanicNotFound() {

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(mechanicRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> serviceRequestService
                        .createServiceRequest(requestDto)
        );

        verify(vehicleRepository).findById(1L);
        verify(mechanicRepository).findById(1L);

        verify(serviceRequestRepository, never())
                .save(any(ServiceRequest.class));
    }

    @Test
    void getServiceRequestById_success() {

        when(serviceRequestRepository.findById(1L))
                .thenReturn(Optional.of(serviceRequest));

        ServiceResponseDto responseDto =
                new ServiceResponseDto();

        when(mapper.toServiceResponseDto(serviceRequest))
                .thenReturn(responseDto);

        ServiceResponseDto result =
                serviceRequestService.getServiceRequestById(1L);

        assertNotNull(result);

        verify(serviceRequestRepository)
                .findById(1L);

        verify(mapper)
                .toServiceResponseDto(serviceRequest);
    }

    @Test
    void getServiceRequestById_notFound() {

        when(serviceRequestRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> serviceRequestService
                        .getServiceRequestById(1L)
        );

        verify(serviceRequestRepository)
                .findById(1L);
    }

    @Test
    void cancelServiceRequest_success() {

        when(serviceRequestRepository.findById(1L))
                .thenReturn(Optional.of(serviceRequest));

        serviceRequest.setStatus(ServiceStatus.REQUESTED);

        serviceRequestService.cancelServiceRequest(1L);

        assertEquals(
                ServiceStatus.CANCELLED,
                serviceRequest.getStatus()
        );

        verify(serviceRequestRepository)
                .save(serviceRequest);
    }
}