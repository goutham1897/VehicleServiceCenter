package com.example.vehicleservice.serviceimpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vehicleservice.constants.ExceptionConstants;
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
import com.example.vehicleservice.service.VehicleService;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private static final Logger logger =
            LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleServiceMapper mapper;

    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            CustomerRepository customerRepository,
            VehicleServiceMapper mapper) {

        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    // =========================================================
    // CREATE VEHICLE
    // =========================================================

    @Override
    public VehicleResponseDto createVehicle(VehicleRequestDto requestDto) {

        logger.info(
                "Creating vehicle with registration number: {}",
                requestDto.getRegistrationNumber()
        );

        // Check duplicate registration number
        if (vehicleRepository.existsByRegistrationNumber(
                requestDto.getRegistrationNumber())) {

            logger.warn(
                    "Vehicle already exists: {}",
                    requestDto.getRegistrationNumber()
            );

            throw new DuplicateResourceException(
                    ExceptionConstants.VEHICLE_REGISTRATION_EXISTS
                            + requestDto.getRegistrationNumber()
            );
        }

        // Find customer
        Customer customer = customerRepository.findById(
                requestDto.getCustomerId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                ExceptionConstants.CUSTOMER_NOT_FOUND
                        + requestDto.getCustomerId()
        ));

        // Convert DTO to entity
        Vehicle vehicle = mapper.toVehicleEntity(requestDto);

        // Set customer relationship
        vehicle.setCustomer(customer);

        // Initial vehicle status
        vehicle.setStatus(VehicleStatus.ACTIVE);

        // Save vehicle
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        logger.info(
                "Vehicle created successfully with id: {}",
                savedVehicle.getId()
        );

        return mapper.toVehicleResponseDto(savedVehicle);
    }

    // =========================================================
    // GET VEHICLE BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public VehicleResponseDto getVehicleById(Long id) {

        logger.info("Fetching vehicle with id: {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ExceptionConstants.VEHICLE_NOT_FOUND + id
                ));

        return mapper.toVehicleResponseDto(vehicle);
    }

    // =========================================================
    // GET ALL VEHICLES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponseDto> getAllVehicles(Pageable pageable) {

        logger.info(
                "Fetching all vehicles - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Vehicle> vehiclePage =
                vehicleRepository.findAll(pageable);

        return vehiclePage.map(mapper::toVehicleResponseDto);
    }

    // =========================================================
    // GET VEHICLES BY CUSTOMER
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponseDto> getVehiclesByCustomer(
            Long customerId,
            Pageable pageable) {

        logger.info(
                "Fetching vehicles for customer id: {}",
                customerId
        );

        // First check customer exists
        if (!customerRepository.existsById(customerId)) {

            throw new ResourceNotFoundException(
                    ExceptionConstants.CUSTOMER_NOT_FOUND
                            + customerId
            );
        }

        Page<Vehicle> vehiclePage =
                vehicleRepository.findByCustomerId(
                        customerId,
                        pageable
                );

        return vehiclePage.map(mapper::toVehicleResponseDto);
    }

    // =========================================================
    // GET VEHICLE BY REGISTRATION NUMBER
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public VehicleResponseDto getVehicleByRegistrationNumber(
            String registrationNumber) {

        logger.info(
                "Fetching vehicle with registration number: {}",
                registrationNumber
        );

        Vehicle vehicle =
                vehicleRepository
                        .findByRegistrationNumber(registrationNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants.VEHICLE_NOT_FOUND
                                                + registrationNumber
                                )
                        );

        return mapper.toVehicleResponseDto(vehicle);
    }

    // =========================================================
    // UPDATE VEHICLE
    // =========================================================

    @Override
    public VehicleResponseDto updateVehicle(
            Long id,
            VehicleRequestDto requestDto) {

        logger.info("Updating vehicle with id: {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ExceptionConstants.VEHICLE_NOT_FOUND + id
                ));

        // Check registration number only if it is changed
        if (!vehicle.getRegistrationNumber()
                .equalsIgnoreCase(requestDto.getRegistrationNumber())) {

            if (vehicleRepository.existsByRegistrationNumber(
                    requestDto.getRegistrationNumber())) {

                throw new DuplicateResourceException(
                        ExceptionConstants.VEHICLE_REGISTRATION_EXISTS
                                + requestDto.getRegistrationNumber()
                );
            }

            vehicle.setRegistrationNumber(
                    requestDto.getRegistrationNumber()
            );
        }

        // Find new customer
        Customer customer = customerRepository.findById(
                requestDto.getCustomerId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                ExceptionConstants.CUSTOMER_NOT_FOUND
                        + requestDto.getCustomerId()
        ));

        // Update vehicle details
        vehicle.setBrand(requestDto.getBrand());
        vehicle.setModel(requestDto.getModel());
        vehicle.setVehicleType(requestDto.getVehicleType());
        vehicle.setFuelType(requestDto.getFuelType());
        vehicle.setManufacturingYear(
                requestDto.getManufacturingYear()
        );
        vehicle.setColor(requestDto.getColor());

        // Update customer
        vehicle.setCustomer(customer);

        Vehicle updatedVehicle =
                vehicleRepository.save(vehicle);

        logger.info(
                "Vehicle updated successfully with id: {}",
                id
        );

        return mapper.toVehicleResponseDto(updatedVehicle);
    }

    // =========================================================
    // DELETE VEHICLE
    // =========================================================

    @Override
    public void deleteVehicle(Long id) {

        logger.info("Deleting vehicle with id: {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ExceptionConstants.VEHICLE_NOT_FOUND + id
                ));

        vehicleRepository.delete(vehicle);

        logger.info(
                "Vehicle deleted successfully with id: {}",
                id
        );
    }
}