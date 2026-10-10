package com.example.vehicleservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class PartRequestDto {

    @NotBlank(message = "Part name is required")
    private String partName;

    @NotBlank(message = "Part number is required")
    private String partNumber;

    @Min(
        value = 0,
        message = "Quantity cannot be negative"
    )
    private Integer availableQuantity;

    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Unit price must be greater than zero"
    )
    private BigDecimal unitPrice;

    private String manufacturer;


    public PartRequestDto() {
    }


    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }


    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }


    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }


    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }


    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}