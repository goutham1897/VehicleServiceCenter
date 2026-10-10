package com.example.vehicleservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.vehicleservice.dto.InvoiceRequestDto;
import com.example.vehicleservice.dto.InvoiceResponseDto;
import com.example.vehicleservice.enums.InvoiceStatus;
import com.example.vehicleservice.service.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(
            InvoiceService invoiceService) {

        this.invoiceService = invoiceService;
    }

    // =========================================================
    // CREATE INVOICE
    // =========================================================

    @PostMapping
    public ResponseEntity<InvoiceResponseDto> createInvoice(
            @Valid
            @RequestBody
            InvoiceRequestDto requestDto) {

        InvoiceResponseDto response =
                invoiceService.createInvoice(
                        requestDto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET INVOICE BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(
            @PathVariable Long id) {

        InvoiceResponseDto response =
                invoiceService.getInvoiceById(id);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET INVOICE BY SERVICE REQUEST
    // =========================================================

    @GetMapping("/service-request/{serviceRequestId}")
    public ResponseEntity<InvoiceResponseDto>
            getInvoiceByServiceRequest(

                    @PathVariable
                    Long serviceRequestId) {

        InvoiceResponseDto response =
                invoiceService
                        .getInvoiceByServiceRequest(
                                serviceRequestId
                        );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ALL INVOICES
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<InvoiceResponseDto>>
            getAllInvoices(

                    @RequestParam(
                            defaultValue = "0")
                    int page,

                    @RequestParam(
                            defaultValue = "10")
                    int size,

                    @RequestParam(
                            defaultValue = "invoiceDate")
                    String sortBy,

                    @RequestParam(
                            defaultValue = "desc")
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

        Page<InvoiceResponseDto> response =
                invoiceService.getAllInvoices(
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET INVOICES BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<InvoiceResponseDto>>
            getInvoicesByStatus(

                    @PathVariable
                    InvoiceStatus status,

                    @RequestParam(
                            defaultValue = "0")
                    int page,

                    @RequestParam(
                            defaultValue = "10")
                    int size,

                    @RequestParam(
                            defaultValue = "invoiceDate")
                    String sortBy,

                    @RequestParam(
                            defaultValue = "desc")
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

        Page<InvoiceResponseDto> response =
                invoiceService.getInvoicesByStatus(
                        status,
                        pageable
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE INVOICE STATUS
    // =========================================================

    @PutMapping("/{id}/status")
    public ResponseEntity<InvoiceResponseDto>
            updateInvoiceStatus(

                    @PathVariable Long id,

                    @RequestParam
                    InvoiceStatus status) {

        InvoiceResponseDto response =
                invoiceService.updateInvoiceStatus(
                        id,
                        status
                );

        return ResponseEntity.ok(response);
    }
}