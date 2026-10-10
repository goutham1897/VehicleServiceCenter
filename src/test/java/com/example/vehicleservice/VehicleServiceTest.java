package com.example.vehicleservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.vehicleservice.dto.VehicleRequestDto;
import com.example.vehicleservice.dto.VehicleResponseDto;
import com.example.vehicleservice.entity.Customer;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.enums.VehicleStatus;
import com.example.vehicleservice.exception.DuplicateResourceException;
import com.example.vehicleservice.exception.ResourceNotFoundException;
import com.example.vehicleservice.mapper.VehicleServiceMapper;
import com.example.vehicleservice.repository.CustomerRepository;
import com.example.vehicleservice.repository.VehicleRepository;
import com.example.vehicleservice.serviceimpl.VehicleServiceImpl;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private VehicleRequestDto requestDto;
    private Vehicle vehicle;
    private Customer customer;
    private VehicleResponseDto responseDto;

    @BeforeEach
    void setUp() {

        requestDto = new VehicleRequestDto();

        requestDto.setRegistrationNumber("TN38AB1234");
        requestDto.setBrand("Toyota");
        requestDto.setModel("Innova");
        requestDto.setVehicleType("SUV");
        requestDto.setFuelType("Diesel");
        requestDto.setManufacturingYear(2022);
        requestDto.setColor("White");
        requestDto.setCustomerId(1L);

        customer = new Customer();

        customer.setId(1L);
        customer.setName("Goutham");

        vehicle = new Vehicle();

        vehicle.setId(1L);
        vehicle.setRegistrationNumber("TN38AB1234");
        vehicle.setBrand("Toyota");
        vehicle.setModel("Innova");
        vehicle.setVehicleType("SUV");
        vehicle.setFuelType("Diesel");
        vehicle.setManufacturingYear(2022);
        vehicle.setColor("White");
        vehicle.setStatus(VehicleStatus.ACTIVE);
        vehicle.setCustomer(customer);

        responseDto = new VehicleResponseDto();

        responseDto.setId(1L);
        responseDto.setRegistrationNumber("TN38AB1234");
        responseDto.setBrand("Toyota");
        responseDto.setModel("Innova");
        responseDto.setVehicleType("SUV");
        responseDto.setFuelType("Diesel");
        responseDto.setManufacturingYear(2022);
        responseDto.setColor("White");
        responseDto.setStatus(VehicleStatus.ACTIVE);
        responseDto.setCustomerId(1L);
        responseDto.setCustomerName("Goutham");
    }

    @Test
    void createVehicle_shouldCreateSuccessfully() {

        when(vehicleRepository.existsByRegistrationNumber(
                requestDto.getRegistrationNumber()))
                .thenReturn(false);

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(mapper.toVehicleEntity(requestDto))
                .thenReturn(vehicle);

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(vehicle);

        when(mapper.toVehicleResponseDto(vehicle))
                .thenReturn(responseDto);

        VehicleResponseDto result =
                vehicleService.createVehicle(requestDto);

        assertNotNull(result);

        assertEquals(
                "TN38AB1234",
                result.getRegistrationNumber()
        );

        assertEquals(
                "Toyota",
                result.getBrand()
        );

        verify(vehicleRepository)
                .save(any(Vehicle.class));
    }

    @Test
    void createVehicle_shouldThrowExceptionForDuplicateRegistration() {

        when(vehicleRepository.existsByRegistrationNumber(
                requestDto.getRegistrationNumber()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> vehicleService
                        .createVehicle(requestDto)
        );

        verify(vehicleRepository, never())
                .save(any(Vehicle.class));
    }

    @Test
    void createVehicle_shouldThrowExceptionWhenCustomerNotFound() {

        when(vehicleRepository.existsByRegistrationNumber(
                requestDto.getRegistrationNumber()))
                .thenReturn(false);

        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService
                        .createVehicle(requestDto)
        );

        verify(vehicleRepository, never())
                .save(any(Vehicle.class));
    }

    @Test
    void getVehicleById_shouldReturnVehicle() {

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(mapper.toVehicleResponseDto(vehicle))
                .thenReturn(responseDto);

        VehicleResponseDto result =
                vehicleService.getVehicleById(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "TN38AB1234",
                result.getRegistrationNumber()
        );

        verify(vehicleRepository)
                .findById(1L);
    }

    @Test
    void getVehicleById_shouldThrowExceptionWhenNotFound() {

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService
                        .getVehicleById(1L)
        );
    }

    @Test
    void deleteVehicle_shouldDeleteSuccessfully() {

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(1L);

        verify(vehicleRepository)
                .delete(vehicle);
    }
}