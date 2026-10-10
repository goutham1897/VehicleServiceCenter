package com.example.vehicleservice.dto;

import com.example.vehicleservice.enums.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ServiceRequestDto {

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    private Long mechanicId;

    @NotNull(message = "Service type is required")
    private ServiceType serviceType;

    @NotBlank(message = "Problem description is required")
    private String problemDescription;

    private BigDecimal estimatedCost;


    public ServiceRequestDto() {
    }


    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }


    public Long getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(Long mechanicId) {
        this.mechanicId = mechanicId;
    }


    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }


    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(
            String problemDescription) {

        this.problemDescription = problemDescription;
    }


    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}