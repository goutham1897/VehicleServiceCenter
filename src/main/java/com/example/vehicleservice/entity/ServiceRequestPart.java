package com.example.vehicleservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Entity
@Table(
    name = "service_request_parts",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_service_request_part",
            columnNames = {
                "service_request_id",
                "part_id"
            }
        )
    }
)
public class ServiceRequestPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "service_request_id",
        nullable = false
    )
    private ServiceRequest serviceRequest;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "part_id",
        nullable = false
    )
    private Part part;


    @Min(
        value = 1,
        message = "Quantity must be at least 1"
    )
    @Column(nullable = false)
    private Integer quantity;


    @Column(
        name = "unit_price",
        precision = 12,
        scale = 2,
        nullable = false
    )
    private BigDecimal unitPrice;


    @Column(
        name = "total_price",
        precision = 12,
        scale = 2,
        nullable = false
    )
    private BigDecimal totalPrice;


    public ServiceRequestPart() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }


    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }


    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}