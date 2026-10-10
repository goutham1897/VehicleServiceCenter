package com.example.vehicleservice.dto;

import com.example.vehicleservice.enums.VehicleStatus;

import java.time.LocalDateTime;

public class VehicleResponseDto {

    private Long id;

    private String registrationNumber;

    private String brand;

    private String model;

    private String vehicleType;

    private String fuelType;

    private Integer manufacturingYear;

    private String color;

    private VehicleStatus status;

    private Long customerId;

    private String customerName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public VehicleResponseDto() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(
            String registrationNumber) {

        this.registrationNumber = registrationNumber;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }


    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }


    public Integer getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(
            Integer manufacturingYear) {

        this.manufacturingYear = manufacturingYear;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
}