package com.example.vehicleservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleRequestDto {

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Vehicle brand is required")
    private String brand;

    @NotBlank(message = "Vehicle model is required")
    private String model;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    private String fuelType;

    @Min(
        value = 1900,
        message = "Invalid manufacturing year"
    )
    private Integer manufacturingYear;

    private String color;

    @NotNull(message = "Customer ID is required")
    private Long customerId;


    public VehicleRequestDto() {
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


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}