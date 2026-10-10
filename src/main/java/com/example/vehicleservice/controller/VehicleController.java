package com.example.vehicleservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.vehicleservice.dto.VehicleRequestDto;
import com.example.vehicleservice.dto.VehicleResponseDto;
import com.example.vehicleservice.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(
            VehicleService vehicleService) {

        this.vehicleService = vehicleService;
    }

    // =========================================================
    // CREATE VEHICLE
    // =========================================================

    @PostMapping
    public ResponseEntity<VehicleResponseDto> createVehicle(
            @Valid @RequestBody VehicleRequestDto requestDto) {

        VehicleResponseDto response =
                vehicleService.createVehicle(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET VEHICLE BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> getVehicleById(
            @PathVariable Long id) {

        VehicleResponseDto response =
                vehicleService.getVehicleById(id);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ALL VEHICLES
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<VehicleResponseDto>> getAllVehicles(

            @RequestParam(
                    defaultValue = "0")
            int page,

            @RequestParam(
                    defaultValue = "10")
            int size,

            @RequestParam(
                    defaultValue = "id")
            String sortBy,

            @RequestParam(
                    defaultValue = "asc")
            String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {

            sort = Sort.by(sortBy).descending();

        } else {

            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        Page<VehicleResponseDto> response =
                vehicleService.getAllVehicles(
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET VEHICLES BY CUSTOMER
    // =========================================================

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<VehicleResponseDto>>
            getVehiclesByCustomer(

                    @PathVariable Long customerId,

                    @RequestParam(
                            defaultValue = "0")
                    int page,

                    @RequestParam(
                            defaultValue = "10")
                    int size,

                    @RequestParam(
                            defaultValue = "id")
                    String sortBy,

                    @RequestParam(
                            defaultValue = "asc")
                    String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {

            sort = Sort.by(sortBy).descending();

        } else {

            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        Page<VehicleResponseDto> response =
                vehicleService.getVehiclesByCustomer(
                        customerId,
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET VEHICLE BY REGISTRATION NUMBER
    // =========================================================

    @GetMapping("/registration/{registrationNumber}")
    public ResponseEntity<VehicleResponseDto>
            getVehicleByRegistrationNumber(

                    @PathVariable
                    String registrationNumber) {

        VehicleResponseDto response =
                vehicleService
                        .getVehicleByRegistrationNumber(
                                registrationNumber
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE VEHICLE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(

            @PathVariable Long id,

            @Valid
            @RequestBody
            VehicleRequestDto requestDto) {

        VehicleResponseDto response =
                vehicleService.updateVehicle(
                        id,
                        requestDto
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE VEHICLE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id) {

        vehicleService.deleteVehicle(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}