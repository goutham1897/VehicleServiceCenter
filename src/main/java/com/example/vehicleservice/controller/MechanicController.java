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

import com.example.vehicleservice.dto.MechanicRequestDto;
import com.example.vehicleservice.dto.MechanicResponseDto;
import com.example.vehicleservice.enums.MechanicStatus;
import com.example.vehicleservice.service.MechanicService;

@RestController
@RequestMapping("/api/mechanics")
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(
            MechanicService mechanicService) {

        this.mechanicService = mechanicService;
    }

    // =========================================================
    // CREATE MECHANIC
    // =========================================================

    @PostMapping
    public ResponseEntity<MechanicResponseDto> createMechanic(
            @Valid
            @RequestBody
            MechanicRequestDto requestDto) {

        MechanicResponseDto response =
                mechanicService.createMechanic(
                        requestDto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET MECHANIC BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<MechanicResponseDto> getMechanicById(
            @PathVariable Long id) {

        MechanicResponseDto response =
                mechanicService.getMechanicById(id);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ALL MECHANICS
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<MechanicResponseDto>>
            getAllMechanics(

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

        Page<MechanicResponseDto> response =
                mechanicService.getAllMechanics(
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET MECHANICS BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<MechanicResponseDto>>
            getMechanicsByStatus(

                    @PathVariable
                    MechanicStatus status,

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

        Page<MechanicResponseDto> response =
                mechanicService.getMechanicsByStatus(
                        status,
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET AVAILABLE MECHANICS
    // =========================================================

    @GetMapping("/available")
    public ResponseEntity<Page<MechanicResponseDto>>
            getAvailableMechanics(

                    @RequestParam(
                            defaultValue = "0")
                    int page,

                    @RequestParam(
                            defaultValue = "10")
                    int size,

                    @RequestParam(
                            defaultValue = "name")
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

        Page<MechanicResponseDto> response =
                mechanicService.getAvailableMechanics(
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE MECHANIC
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<MechanicResponseDto>
            updateMechanic(

                    @PathVariable Long id,

                    @Valid
                    @RequestBody
                    MechanicRequestDto requestDto) {

        MechanicResponseDto response =
                mechanicService.updateMechanic(
                        id,
                        requestDto
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE MECHANIC
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMechanic(
            @PathVariable Long id) {

        mechanicService.deleteMechanic(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}