
package com.example.vehicleservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.vehicleservice.dto.ServiceRequestPartRequestDto;
import com.example.vehicleservice.entity.ServiceRequestPart;
import com.example.vehicleservice.service.ServiceRequestPartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/service-request-parts")
public class ServiceRequestPartController {

    private final ServiceRequestPartService service;

    public ServiceRequestPartController(
            ServiceRequestPartService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ServiceRequestPart> addPart(
            @Valid @RequestBody ServiceRequestPartRequestDto request) {

        ServiceRequestPart savedPart =
                service.addPartToServiceRequest(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPart);
    }
}
