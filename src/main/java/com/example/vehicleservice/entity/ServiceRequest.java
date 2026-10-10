package com.example.vehicleservice.entity;

import com.example.vehicleservice.enums.ServiceStatus;
import com.example.vehicleservice.enums.ServiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_requests")
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Service request number is required")
    @Column(
        name = "request_number",
        nullable = false,
        unique = true
    )
    private String requestNumber;


    @NotNull(message = "Service type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;


    @NotBlank(message = "Problem description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String problemDescription;


    @NotNull(message = "Service status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;


    private LocalDateTime requestDate;

    private LocalDateTime serviceStartDate;

    private LocalDateTime serviceCompletedDate;


    @Column(precision = 12, scale = 2)
    private BigDecimal estimatedCost;


    @Column(precision = 12, scale = 2)
    private BigDecimal actualCost;


    // Many Service Requests belong to One Vehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "vehicle_id",
        nullable = false
    )
    private Vehicle vehicle;


    // Many Service Requests can be assigned to One Mechanic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id")
    private Mechanic mechanic;


    // One Service Request can use Many Parts
    @OneToMany(
        mappedBy = "serviceRequest",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ServiceRequestPart> serviceRequestParts =
            new ArrayList<>();


    // One Service Request has One Invoice
    @OneToOne(
        mappedBy = "serviceRequest",
        cascade = CascadeType.ALL
    )
    private Invoice invoice;


    public ServiceRequest() {
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

    public void setProblemDescription(String problemDescription) {
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

    public void setServiceStartDate(LocalDateTime serviceStartDate) {
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


    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }


    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }


    public List<ServiceRequestPart> getServiceRequestParts() {
        return serviceRequestParts;
    }

    public void setServiceRequestParts(
            List<ServiceRequestPart> serviceRequestParts) {

        this.serviceRequestParts = serviceRequestParts;
    }


    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}