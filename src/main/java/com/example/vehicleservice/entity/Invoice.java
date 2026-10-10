package com.example.vehicleservice.entity;

import com.example.vehicleservice.enums.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
        name = "invoice_number",
        nullable = false,
        unique = true
    )
    private String invoiceNumber;


    @NotNull(message = "Service charge is required")
    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Service charge cannot be negative"
    )
    @Column(precision = 12, scale = 2)
    private BigDecimal serviceCharge;


    @NotNull(message = "Parts charge is required")
    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Parts charge cannot be negative"
    )
    @Column(precision = 12, scale = 2)
    private BigDecimal partsCharge;


    @Column(precision = 12, scale = 2)
    private BigDecimal tax;


    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;


    private LocalDateTime invoiceDate;

    private LocalDateTime paidDate;


    // One Invoice belongs to One Service Request
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "service_request_id",
        nullable = false,
        unique = true
    )
    private ServiceRequest serviceRequest;


    public Invoice() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }


    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }


    public BigDecimal getPartsCharge() {
        return partsCharge;
    }

    public void setPartsCharge(BigDecimal partsCharge) {
        this.partsCharge = partsCharge;
    }


    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }


    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }


    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }


    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }
}