package com.example.vehicleservice.controller;

import java.util.List;

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

import com.example.vehicleservice.dto.ServiceRequestDto;
import com.example.vehicleservice.dto.ServiceResponseDto;
import com.example.vehicleservice.enums.ServiceStatus;
import com.example.vehicleservice.service.ServiceRequestService;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(
            ServiceRequestService serviceRequestService) {

        this.serviceRequestService =
                serviceRequestService;
    }

    // =========================================================
    // CREATE SERVICE REQUEST
    // =========================================================

    @PostMapping
    public ResponseEntity<ServiceResponseDto>
            createServiceRequest(

                    @Valid
                    @RequestBody
                    ServiceRequestDto requestDto) {

        ServiceResponseDto response =
                serviceRequestService
                        .createServiceRequest(
                                requestDto
                        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET SERVICE REQUEST BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponseDto>
            getServiceRequestById(
                    @PathVariable Long id) {

        ServiceResponseDto response =
                serviceRequestService
                        .getServiceRequestById(id);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ALL SERVICE REQUESTS
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<ServiceResponseDto>>
            getAllServiceRequests(

                    @RequestParam(
                            defaultValue = "0")
                    int page,

                    @RequestParam(
                            defaultValue = "10")
                    int size,

                    @RequestParam(
                            defaultValue = "requestDate")
                    String sortBy,

                    @RequestParam(
                            defaultValue = "desc")
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

        Page<ServiceResponseDto> response =
                serviceRequestService
                        .getAllServiceRequests(
                                pageable
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET SERVICE REQUESTS BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ServiceResponseDto>>
            getServiceRequestsByStatus(

                    @PathVariable
                    ServiceStatus status,

                    @RequestParam(
                            defaultValue = "0")
                    int page,

                    @RequestParam(
                            defaultValue = "10")
                    int size,

                    @RequestParam(
                            defaultValue = "requestDate")
                    String sortBy,

                    @RequestParam(
                            defaultValue = "desc")
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

        Page<ServiceResponseDto> response =
                serviceRequestService
                        .getServiceRequestsByStatus(
                                status,
                                pageable
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // SERVICE HISTORY BY VEHICLE
    // =========================================================

    @GetMapping("/history/vehicle/{vehicleId}")
    public ResponseEntity<Page<ServiceResponseDto>>
            getServiceHistoryByVehicle(

                    @PathVariable
                    Long vehicleId,

                    @RequestParam(
                            defaultValue = "0")
                    int page,

                    @RequestParam(
                            defaultValue = "10")
                    int size) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("requestDate")
                                .descending()
                );

        Page<ServiceResponseDto> response =
                serviceRequestService
                        .getServiceHistoryByVehicle(
                                vehicleId,
                                pageable
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // SERVICE HISTORY BY CUSTOMER
    // =========================================================

    @GetMapping("/history/customer/{customerId}")
    public ResponseEntity<List<ServiceResponseDto>>
            getServiceHistoryByCustomer(

                    @PathVariable
                    Long customerId) {

        List<ServiceResponseDto> response =
                serviceRequestService
                        .getServiceHistoryByCustomer(
                                customerId
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // JOBS BY MECHANIC
    // =========================================================

    @GetMapping("/mechanic/{mechanicId}")
    public ResponseEntity<List<ServiceResponseDto>>
            getJobsByMechanic(

                    @PathVariable
                    Long mechanicId) {

        List<ServiceResponseDto> response =
                serviceRequestService
                        .getJobsByMechanic(
                                mechanicId
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE SERVICE REQUEST
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponseDto>
            updateServiceRequest(

                    @PathVariable Long id,

                    @Valid
                    @RequestBody
                    ServiceRequestDto requestDto) {

        ServiceResponseDto response =
                serviceRequestService
                        .updateServiceRequest(
                                id,
                                requestDto
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ASSIGN MECHANIC
    // =========================================================

    @PutMapping("/{serviceRequestId}/mechanic/{mechanicId}")
    public ResponseEntity<ServiceResponseDto>
            assignMechanic(

                    @PathVariable
                    Long serviceRequestId,

                    @PathVariable
                    Long mechanicId) {

        ServiceResponseDto response =
                serviceRequestService
                        .assignMechanic(
                                serviceRequestId,
                                mechanicId
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE JOB STATUS
    // =========================================================

    @PutMapping("/{id}/status")
    public ResponseEntity<ServiceResponseDto>
            updateJobStatus(

                    @PathVariable
                    Long id,

                    @RequestParam
                    ServiceStatus status) {

        ServiceResponseDto response =
                serviceRequestService
                        .updateJobStatus(
                                id,
                                status
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // CANCEL SERVICE REQUEST
    // =========================================================

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<ServiceResponseDto>
            cancelServiceRequest(

                    @PathVariable
                    Long id) {

        ServiceResponseDto response =
                serviceRequestService
                        .cancelServiceRequest(
                                id
                        );

        return ResponseEntity.ok(response);
    }
}