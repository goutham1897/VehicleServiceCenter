package com.example.vehicleservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvoiceRequestDto {

    @NotNull(message = "Service request ID is required")
    private Long serviceRequestId;

    @NotNull(message = "Service charge is required")
    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Service charge cannot be negative"
    )
    private BigDecimal serviceCharge;

    private BigDecimal tax;


    public InvoiceRequestDto() {
    }


    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }


    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }


    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}