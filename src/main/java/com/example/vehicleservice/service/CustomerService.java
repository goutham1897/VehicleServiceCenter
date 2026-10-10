package com.example.vehicleservice.service;

import com.example.vehicleservice.dto.CustomerRequestDto;
import com.example.vehicleservice.dto.CustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerResponseDto createCustomer(
            CustomerRequestDto requestDto
    );

    CustomerResponseDto getCustomerById(
            Long id
    );

    Page<CustomerResponseDto> getAllCustomers(
            Pageable pageable
    );

    CustomerResponseDto updateCustomer(
            Long id,
            CustomerRequestDto requestDto
    );

    void deleteCustomer(
            Long id
    );
}