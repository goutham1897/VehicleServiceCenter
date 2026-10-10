package com.example.vehicleservice.dto;

import com.example.vehicleservice.enums.MechanicStatus;

import java.time.LocalDateTime;

public class MechanicResponseDto {

    private Long id;

    private String name;

    private String phone;

    private String email;

    private String specialization;

    private MechanicStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public MechanicResponseDto() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }


    public MechanicStatus getStatus() {
        return status;
    }

    public void setStatus(MechanicStatus status) {
        this.status = status;
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