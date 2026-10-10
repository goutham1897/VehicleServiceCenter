package com.example.vehicleservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.vehicleservice.dto.InvoiceRequestDto;
import com.example.vehicleservice.dto.InvoiceResponseDto;
import com.example.vehicleservice.enums.InvoiceStatus;

public interface InvoiceService {

    InvoiceResponseDto createInvoice(
            InvoiceRequestDto requestDto);

    InvoiceResponseDto getInvoiceById(
            Long id);

    InvoiceResponseDto getInvoiceByServiceRequest(
            Long serviceRequestId);

    Page<InvoiceResponseDto> getAllInvoices(
            Pageable pageable);

    Page<InvoiceResponseDto> getInvoicesByStatus(
            InvoiceStatus status,
            Pageable pageable);

    InvoiceResponseDto updateInvoiceStatus(
            Long id,
            InvoiceStatus status);
}