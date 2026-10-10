package com.example.vehicleservice.dto;

import com.example.vehicleservice.enums.ServiceStatus;
import com.example.vehicleservice.enums.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServiceResponseDto {

    private Long id;

    private String requestNumber;

    private ServiceType serviceType;

    private String problemDescription;

    private ServiceStatus status;

    private LocalDateTime requestDate;

    private LocalDateTime serviceStartDate;

    private LocalDateTime serviceCompletedDate;

    private BigDecimal estimatedCost;

    private BigDecimal actualCost;

    private Long vehicleId;

    private String vehicleRegistrationNumber;

    private Long mechanicId;

    private String mechanicName;


    public ServiceResponseDto() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
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


    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }


    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }


    public LocalDateTime getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(
            LocalDateTime serviceStartDate) {

        this.serviceStartDate = serviceStartDate;
    }


    public LocalDateTime getServiceCompletedDate() {
        return serviceCompletedDate;
    }

    public void setServiceCompletedDate(
            LocalDateTime serviceCompletedDate) {

        this.serviceCompletedDate = serviceCompletedDate;
    }


    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }


    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }


    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }


    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(
            String vehicleRegistrationNumber) {

        this.vehicleRegistrationNumber =
                vehicleRegistrationNumber;
    }


    public Long getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(Long mechanicId) {
        this.mechanicId = mechanicId;
    }


    public String getMechanicName() {
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) {
        this.mechanicName = mechanicName;
    }
}