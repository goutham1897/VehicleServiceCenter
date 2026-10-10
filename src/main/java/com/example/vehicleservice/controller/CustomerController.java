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

import com.example.vehicleservice.dto.CustomerRequestDto;
import com.example.vehicleservice.dto.CustomerResponseDto;
import com.example.vehicleservice.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService) {

        this.customerService = customerService;
    }

    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid @RequestBody CustomerRequestDto requestDto) {

        CustomerResponseDto response =
                customerService.createCustomer(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET CUSTOMER BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(
            @PathVariable Long id) {

        CustomerResponseDto response =
                customerService.getCustomerById(id);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> getAllCustomers(

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

        Page<CustomerResponseDto> response =
                customerService.getAllCustomers(
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDto requestDto) {

        CustomerResponseDto response =
                customerService.updateCustomer(
                        id,
                        requestDto
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE CUSTOMER
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }
}