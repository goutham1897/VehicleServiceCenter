package com.example.vehicleservice.repository;

import com.example.vehicleservice.entity.Invoice;
import com.example.vehicleservice.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(
            String invoiceNumber
    );

    boolean existsByInvoiceNumber(
            String invoiceNumber
    );

    Optional<Invoice> findByServiceRequestId(
            Long serviceRequestId
    );

    Page<Invoice> findByStatus(
            InvoiceStatus status,
            Pageable pageable
    );
}