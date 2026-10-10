package com.example.vehicleservice.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vehicleservice.constants.ExceptionConstants;
import com.example.vehicleservice.dto.InvoiceRequestDto;
import com.example.vehicleservice.dto.InvoiceResponseDto;
import com.example.vehicleservice.entity.Invoice;
import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.enums.InvoiceStatus;
import com.example.vehicleservice.enums.ServiceStatus;
import com.example.vehicleservice.exception.BusinessException;
import com.example.vehicleservice.exception.DuplicateResourceException;
import com.example.vehicleservice.exception.ResourceNotFoundException;
import com.example.vehicleservice.mapper.VehicleServiceMapper;
import com.example.vehicleservice.repository.InvoiceRepository;
import com.example.vehicleservice.repository.ServiceRequestRepository;
import com.example.vehicleservice.service.InvoiceService;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger logger =
            LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final VehicleServiceMapper mapper;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            ServiceRequestRepository serviceRequestRepository,
            VehicleServiceMapper mapper) {

        this.invoiceRepository = invoiceRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.mapper = mapper;
    }

    // =========================================================
    // CREATE INVOICE
    // =========================================================

    @Override
    public InvoiceResponseDto createInvoice(
            InvoiceRequestDto requestDto) {

        logger.info(
                "Creating invoice for service request id: {}",
                requestDto.getServiceRequestId()
        );

        // -----------------------------------------------------
        // 1. Find service request
        // -----------------------------------------------------

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(
                        requestDto.getServiceRequestId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants
                                        .SERVICE_REQUEST_NOT_FOUND
                                        + requestDto
                                        .getServiceRequestId()
                        )
                );

        // -----------------------------------------------------
        // 2. Service must be completed
        // -----------------------------------------------------

        if (serviceRequest.getStatus()
                != ServiceStatus.COMPLETED) {

            throw new BusinessException(
                    "Invoice can be generated only for a completed service"
            );
        }

        // -----------------------------------------------------
        // 3. Check existing invoice
        // -----------------------------------------------------

        if (invoiceRepository
                .findByServiceRequestId(
                        requestDto.getServiceRequestId()
                ).isPresent()) {

            throw new DuplicateResourceException(
                    ExceptionConstants.INVOICE_ALREADY_EXISTS
                            + requestDto.getServiceRequestId()
            );
        }

        // -----------------------------------------------------
        // 4. Calculate service charge
        // -----------------------------------------------------

        BigDecimal serviceCharge =
                requestDto.getServiceCharge();

        if (serviceCharge == null) {
            serviceCharge = BigDecimal.ZERO;
        }

        // -----------------------------------------------------
        // 5. Calculate parts charge
        // -----------------------------------------------------

        BigDecimal partsCharge =
                calculatePartsCharge(serviceRequest);

        // -----------------------------------------------------
        // 6. Tax
        // -----------------------------------------------------

        BigDecimal tax = requestDto.getTax();

        if (tax == null) {
            tax = BigDecimal.ZERO;
        }

        // -----------------------------------------------------
        // 7. Calculate total
        // -----------------------------------------------------

        BigDecimal totalAmount =
                serviceCharge
                        .add(partsCharge)
                        .add(tax)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        // -----------------------------------------------------
        // 8. Create invoice
        // -----------------------------------------------------

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(
                generateInvoiceNumber()
        );

        invoice.setServiceCharge(
                serviceCharge
        );

        invoice.setPartsCharge(
                partsCharge
        );

        invoice.setTax(tax);

        invoice.setTotalAmount(
                totalAmount
        );

        invoice.setStatus(
                InvoiceStatus.GENERATED
        );

        invoice.setInvoiceDate(
                LocalDateTime.now()
        );

        invoice.setServiceRequest(
                serviceRequest
        );

        // -----------------------------------------------------
        // 9. Save
        // -----------------------------------------------------

        Invoice savedInvoice =
                invoiceRepository.save(invoice);

        logger.info(
                "Invoice created successfully: {}",
                savedInvoice.getInvoiceNumber()
        );

        return mapper.toInvoiceResponseDto(
                savedInvoice
        );
    }

    // =========================================================
    // CALCULATE PARTS CHARGE
    // =========================================================

    private BigDecimal calculatePartsCharge(
            ServiceRequest serviceRequest) {

        if (serviceRequest.getServiceRequestParts() == null
                || serviceRequest
                    .getServiceRequestParts()
                    .isEmpty()) {

            return BigDecimal.ZERO;
        }

        return serviceRequest
                .getServiceRequestParts()
                .stream()
                .map(partItem -> {

                    BigDecimal quantity =
                            BigDecimal.valueOf(
                                    partItem.getQuantity()
                            );

                    BigDecimal unitPrice =
                            partItem.getUnitPrice();

                    return unitPrice.multiply(quantity);

                })
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                )
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    // =========================================================
    // GENERATE INVOICE NUMBER
    // =========================================================

    private String generateInvoiceNumber() {

        return "INV-" + System.currentTimeMillis();
    }

    // =========================================================
    // GET INVOICE BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceById(Long id) {

        logger.info(
                "Fetching invoice with id: {}",
                id
        );

        Invoice invoice =
                invoiceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants
                                                .INVOICE_NOT_FOUND
                                                + id
                                )
                        );

        return mapper.toInvoiceResponseDto(invoice);
    }

    // =========================================================
    // GET INVOICE BY SERVICE REQUEST
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceByServiceRequest(
            Long serviceRequestId) {

        logger.info(
                "Fetching invoice for service request id: {}",
                serviceRequestId
        );

        Invoice invoice =
                invoiceRepository
                        .findByServiceRequestId(
                                serviceRequestId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants
                                                .INVOICE_NOT_FOUND
                                                + serviceRequestId
                                )
                        );

        return mapper.toInvoiceResponseDto(invoice);
    }

    // =========================================================
    // GET ALL INVOICES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponseDto> getAllInvoices(
            Pageable pageable) {

        logger.info(
                "Fetching invoices - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Invoice> invoicePage =
                invoiceRepository.findAll(pageable);

        return invoicePage.map(
                mapper::toInvoiceResponseDto
        );
    }

    // =========================================================
    // GET INVOICES BY STATUS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponseDto> getInvoicesByStatus(
            InvoiceStatus status,
            Pageable pageable) {

        logger.info(
                "Fetching invoices with status: {}",
                status
        );

        Page<Invoice> invoicePage =
                invoiceRepository.findByStatus(
                        status,
                        pageable
                );

        return invoicePage.map(
                mapper::toInvoiceResponseDto
        );
    }

    // =========================================================
    // UPDATE INVOICE STATUS
    // =========================================================

    @Override
    public InvoiceResponseDto updateInvoiceStatus(
            Long id,
            InvoiceStatus status) {

        logger.info(
                "Updating invoice {} to status {}",
                id,
                status
        );

        Invoice invoice =
                invoiceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants
                                                .INVOICE_NOT_FOUND
                                                + id
                                )
                        );

        invoice.setStatus(status);

        // Set payment date when invoice is paid
        if (status == InvoiceStatus.PAID) {

            invoice.setPaidDate(
                    LocalDateTime.now()
            );
        }

        Invoice updatedInvoice =
                invoiceRepository.save(invoice);

        logger.info(
                "Invoice status updated successfully"
        );

        return mapper.toInvoiceResponseDto(
                updatedInvoice
        );
    }
}