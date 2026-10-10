package com.example.vehicleservice.serviceimpl;

import com.example.vehicleservice.constants.ExceptionConstants;
import com.example.vehicleservice.dto.CustomerRequestDto;
import com.example.vehicleservice.dto.CustomerResponseDto;
import com.example.vehicleservice.entity.Customer;
import com.example.vehicleservice.enums.CustomerStatus;
import com.example.vehicleservice.exception.DuplicateResourceException;
import com.example.vehicleservice.exception.ResourceNotFoundException;
import com.example.vehicleservice.mapper.VehicleServiceMapper;
import com.example.vehicleservice.repository.CustomerRepository;
import com.example.vehicleservice.service.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger =
            LoggerFactory.getLogger(CustomerServiceImpl.class);


    private final CustomerRepository customerRepository;

    private final VehicleServiceMapper mapper;


    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            VehicleServiceMapper mapper) {

        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }


    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    @Override
    public CustomerResponseDto createCustomer(
            CustomerRequestDto requestDto) {

        logger.info(
                "Creating customer with email: {}",
                requestDto.getEmail()
        );


        // Check duplicate email
        if (customerRepository.existsByEmail(
                requestDto.getEmail())) {

            logger.warn(
                    "Customer email already exists: {}",
                    requestDto.getEmail()
            );

            throw new DuplicateResourceException(
                    ExceptionConstants.CUSTOMER_EMAIL_EXISTS
                            + requestDto.getEmail()
            );
        }


        // Check duplicate phone
        if (customerRepository.existsByPhone(
                requestDto.getPhone())) {

            logger.warn(
                    "Customer phone already exists: {}",
                    requestDto.getPhone()
            );

            throw new DuplicateResourceException(
                    ExceptionConstants.CUSTOMER_PHONE_EXISTS
                            + requestDto.getPhone()
            );
        }


        // Create entity
        Customer customer = new Customer();

        customer.setName(
                requestDto.getName()
        );

        customer.setEmail(
                requestDto.getEmail()
        );

        customer.setPhone(
                requestDto.getPhone()
        );

        customer.setAddress(
                requestDto.getAddress()
        );


        // Default status
        customer.setStatus(
                CustomerStatus.ACTIVE
        );


        // Save
        Customer savedCustomer =
                customerRepository.save(customer);


        logger.info(
                "Customer created successfully with id: {}",
                savedCustomer.getId()
        );


        return mapper.toCustomerResponseDto(
                savedCustomer
        );
    }


    // =========================================================
    // GET CUSTOMER BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(
            Long id) {

        logger.info(
                "Fetching customer with id: {}",
                id
        );


        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants
                                                .CUSTOMER_NOT_FOUND
                                                + id
                                )
                        );


        return mapper.toCustomerResponseDto(
                customer
        );
    }


    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(
            Pageable pageable) {

        logger.info(
                "Fetching customers - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );


        Page<Customer> customerPage =
                customerRepository.findAll(pageable);


        return customerPage.map(
                mapper::toCustomerResponseDto
        );
    }


    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================

    @Override
    public CustomerResponseDto updateCustomer(
            Long id,
            CustomerRequestDto requestDto) {

        logger.info(
                "Updating customer with id: {}",
                id
        );


        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants
                                                .CUSTOMER_NOT_FOUND
                                                + id
                                )
                        );


        // Check email belongs to another customer
        customerRepository.findByEmail(
                requestDto.getEmail()
        ).ifPresent(existingCustomer -> {

            if (!existingCustomer.getId().equals(id)) {

                throw new DuplicateResourceException(
                        ExceptionConstants
                                .CUSTOMER_EMAIL_EXISTS
                                + requestDto.getEmail()
                );
            }
        });


        // Check phone belongs to another customer
        customerRepository.findByPhone(
                requestDto.getPhone()
        ).ifPresent(existingCustomer -> {

            if (!existingCustomer.getId().equals(id)) {

                throw new DuplicateResourceException(
                        ExceptionConstants
                                .CUSTOMER_PHONE_EXISTS
                                + requestDto.getPhone()
                );
            }
        });


        // Update fields
        customer.setName(
                requestDto.getName()
        );

        customer.setEmail(
                requestDto.getEmail()
        );

        customer.setPhone(
                requestDto.getPhone()
        );

        customer.setAddress(
                requestDto.getAddress()
        );


        Customer updatedCustomer =
                customerRepository.save(customer);


        logger.info(
                "Customer updated successfully: {}",
                id
        );


        return mapper.toCustomerResponseDto(
                updatedCustomer
        );
    }


    // =========================================================
    // DELETE CUSTOMER
    // =========================================================

    @Override
    public void deleteCustomer(Long id) {

        logger.info(
                "Deleting customer with id: {}",
                id
        );


        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants
                                                .CUSTOMER_NOT_FOUND
                                                + id
                                )
                        );


        customerRepository.delete(customer);


        logger.info(
                "Customer deleted successfully: {}",
                id
        );
    }
}