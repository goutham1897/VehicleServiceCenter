package com.example.vehicleservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parts")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Part name is required")
    @Column(nullable = false)
    private String partName;

    @NotBlank(message = "Part number is required")
    @Column(nullable = false, unique = true)
    private String partNumber;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private Integer availableQuantity;

    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Part price must be greater than zero"
    )
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    private String manufacturer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // One Part can be used in Many Service Requests
    @OneToMany(mappedBy = "part")
    private List<ServiceRequestPart> serviceRequestParts =
            new ArrayList<>();


    public Part() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public List<ServiceRequestPart> getServiceRequestParts() {
        return serviceRequestParts;
    }

    public void setServiceRequestParts(
            List<ServiceRequestPart> serviceRequestParts) {

        this.serviceRequestParts = serviceRequestParts;
    }
}