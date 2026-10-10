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

import com.example.vehicleservice.dto.PartRequestDto;
import com.example.vehicleservice.dto.PartResponseDto;
import com.example.vehicleservice.service.PartService;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    // =========================================================
    // CREATE PART
    // =========================================================

    @PostMapping
    public ResponseEntity<PartResponseDto> createPart(
            @Valid @RequestBody PartRequestDto requestDto) {

        PartResponseDto response =
                partService.createPart(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET PART BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<PartResponseDto> getPartById(
            @PathVariable Long id) {

        PartResponseDto response =
                partService.getPartById(id);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ALL PARTS
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<PartResponseDto>> getAllParts(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
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

        Page<PartResponseDto> response =
                partService.getAllParts(pageable);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET AVAILABLE PARTS
    // =========================================================

    @GetMapping("/available")
    public ResponseEntity<Page<PartResponseDto>>
            getAvailableParts(

                    @RequestParam(defaultValue = "0")
                    int page,

                    @RequestParam(defaultValue = "10")
                    int size,

                    @RequestParam(defaultValue = "partName")
                    String sortBy,

                    @RequestParam(defaultValue = "asc")
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

        Page<PartResponseDto> response =
                partService.getAvailableParts(
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE PART
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<PartResponseDto> updatePart(

            @PathVariable Long id,

            @Valid
            @RequestBody
            PartRequestDto requestDto) {

        PartResponseDto response =
                partService.updatePart(
                        id,
                        requestDto
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // REDUCE STOCK
    // =========================================================

    @PutMapping("/{partId}/stock/reduce")
    public ResponseEntity<String> reduceStock(

            @PathVariable Long partId,

            @RequestParam Integer quantity) {

        partService.reduceStock(
                partId,
                quantity
        );

        return ResponseEntity.ok(
                "Part stock reduced successfully"
        );
    }

    // =========================================================
    // DELETE PART
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePart(
            @PathVariable Long id) {

        partService.deletePart(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}