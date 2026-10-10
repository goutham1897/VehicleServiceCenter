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

import com.example.vehicleservice.dto.CustomerRequestDto;
import com.example.vehicleservice.dto.CustomerResponseDto;
import com.example.vehicleservice.entity.Customer;
import com.example.vehicleservice.enums.CustomerStatus;
import com.example.vehicleservice.exception.DuplicateResourceException;
import com.example.vehicleservice.exception.ResourceNotFoundException;
import com.example.vehicleservice.mapper.VehicleServiceMapper;
import com.example.vehicleservice.repository.CustomerRepository;
import com.example.vehicleservice.serviceimpl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequestDto requestDto;

    private Customer customer;

    private CustomerResponseDto responseDto;

    @BeforeEach
    void setUp() {

        requestDto = new CustomerRequestDto();

        requestDto.setName("Goutham");
        requestDto.setEmail("goutham@gmail.com");
        requestDto.setPhone("9876543210");
        requestDto.setAddress("Chennai");

        customer = new Customer();

        customer.setId(1L);
        customer.setName("Goutham");
        customer.setEmail("goutham@gmail.com");
        customer.setPhone("9876543210");
        customer.setAddress("Chennai");
        customer.setStatus(CustomerStatus.ACTIVE);

        responseDto = new CustomerResponseDto();

        responseDto.setId(1L);
        responseDto.setName("Goutham");
        responseDto.setEmail("goutham@gmail.com");
        responseDto.setPhone("9876543210");
        responseDto.setAddress("Chennai");
        responseDto.setStatus(CustomerStatus.ACTIVE);
    }

    @Test
    void createCustomer_shouldCreateSuccessfully() {

        when(customerRepository.existsByEmail(
                requestDto.getEmail()))
                .thenReturn(false);

        when(customerRepository.existsByPhone(
                requestDto.getPhone()))
                .thenReturn(false);

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        when(mapper.toCustomerResponseDto(customer))
                .thenReturn(responseDto);

        CustomerResponseDto result =
                customerService.createCustomer(requestDto);

        assertNotNull(result);
        assertEquals("Goutham", result.getName());
        assertEquals(
                "goutham@gmail.com",
                result.getEmail()
        );

        verify(customerRepository)
                .save(any(Customer.class));
    }

    @Test
    void createCustomer_shouldThrowExceptionForDuplicateEmail() {

        when(customerRepository.existsByEmail(
                requestDto.getEmail()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> customerService
                        .createCustomer(requestDto)
        );

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    @Test
    void getCustomerById_shouldReturnCustomer() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(mapper.toCustomerResponseDto(customer))
                .thenReturn(responseDto);

        CustomerResponseDto result =
                customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Goutham", result.getName());

        verify(customerRepository)
                .findById(1L);
    }

    @Test
    void getCustomerById_shouldThrowExceptionWhenNotFound() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> customerService
                        .getCustomerById(1L)
        );
    }

    @Test
    void deleteCustomer_shouldDeleteSuccessfully() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository)
                .delete(customer);
    }
}