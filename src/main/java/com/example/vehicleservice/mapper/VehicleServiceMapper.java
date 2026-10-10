package com.example.vehicleservice.mapper;

import com.example.vehicleservice.dto.CustomerResponseDto;
import com.example.vehicleservice.dto.InvoiceResponseDto;
import com.example.vehicleservice.dto.MechanicResponseDto;
import com.example.vehicleservice.dto.PartResponseDto;
import com.example.vehicleservice.dto.ServiceResponseDto;
import com.example.vehicleservice.dto.VehicleRequestDto;
import com.example.vehicleservice.dto.VehicleResponseDto;
import com.example.vehicleservice.entity.Customer;
import com.example.vehicleservice.entity.Invoice;
import com.example.vehicleservice.entity.Mechanic;
import com.example.vehicleservice.entity.Part;
import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleServiceMapper {


    // =========================================================
    // CUSTOMER ENTITY → CUSTOMER RESPONSE DTO
    // =========================================================

    public CustomerResponseDto toCustomerResponseDto(
            Customer customer) {

        CustomerResponseDto dto =
                new CustomerResponseDto();

        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.getStatus());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        return dto;
    }


    // =========================================================
    // VEHICLE REQUEST DTO → VEHICLE ENTITY
    // =========================================================

    public Vehicle toVehicleEntity(
            VehicleRequestDto dto) {

        Vehicle vehicle = new Vehicle();

        vehicle.setRegistrationNumber(
                dto.getRegistrationNumber()
        );

        vehicle.setBrand(
                dto.getBrand()
        );

        vehicle.setModel(
                dto.getModel()
        );

        vehicle.setVehicleType(
                dto.getVehicleType()
        );

        vehicle.setFuelType(
                dto.getFuelType()
        );

        vehicle.setManufacturingYear(
                dto.getManufacturingYear()
        );

        vehicle.setColor(
                dto.getColor()
        );

        return vehicle;
    }


    // =========================================================
    // VEHICLE ENTITY → VEHICLE RESPONSE DTO
    // =========================================================

    public VehicleResponseDto toVehicleResponseDto(
            Vehicle vehicle) {

        VehicleResponseDto dto =
                new VehicleResponseDto();

        dto.setId(vehicle.getId());

        dto.setRegistrationNumber(
                vehicle.getRegistrationNumber()
        );

        dto.setBrand(
                vehicle.getBrand()
        );

        dto.setModel(
                vehicle.getModel()
        );

        dto.setVehicleType(
                vehicle.getVehicleType()
        );

        dto.setFuelType(
                vehicle.getFuelType()
        );

        dto.setManufacturingYear(
                vehicle.getManufacturingYear()
        );

        dto.setColor(
                vehicle.getColor()
        );

        dto.setStatus(
                vehicle.getStatus()
        );

        dto.setCreatedAt(
                vehicle.getCreatedAt()
        );

        dto.setUpdatedAt(
                vehicle.getUpdatedAt()
        );


        if (vehicle.getCustomer() != null) {

            dto.setCustomerId(
                    vehicle.getCustomer().getId()
            );

            dto.setCustomerName(
                    vehicle.getCustomer().getName()
            );
        }

        return dto;
    }


    // =========================================================
    // MECHANIC ENTITY → MECHANIC RESPONSE DTO
    // =========================================================

    public MechanicResponseDto toMechanicResponseDto(
            Mechanic mechanic) {

        MechanicResponseDto dto =
                new MechanicResponseDto();

        dto.setId(mechanic.getId());

        dto.setName(
                mechanic.getName()
        );

        dto.setPhone(
                mechanic.getPhone()
        );

        dto.setEmail(
                mechanic.getEmail()
        );

        dto.setSpecialization(
                mechanic.getSpecialization()
        );

        dto.setStatus(
                mechanic.getStatus()
        );

        dto.setCreatedAt(
                mechanic.getCreatedAt()
        );

        dto.setUpdatedAt(
                mechanic.getUpdatedAt()
        );

        return dto;
    }


    // =========================================================
    // PART ENTITY → PART RESPONSE DTO
    // =========================================================

    public PartResponseDto toPartResponseDto(
            Part part) {

        PartResponseDto dto =
                new PartResponseDto();

        dto.setId(part.getId());

        dto.setPartName(
                part.getPartName()
        );

        dto.setPartNumber(
                part.getPartNumber()
        );

        dto.setAvailableQuantity(
                part.getAvailableQuantity()
        );

        dto.setUnitPrice(
                part.getUnitPrice()
        );

        dto.setManufacturer(
                part.getManufacturer()
        );

        dto.setCreatedAt(
                part.getCreatedAt()
        );

        dto.setUpdatedAt(
                part.getUpdatedAt()
        );

        return dto;
    }


    // =========================================================
    // SERVICE REQUEST ENTITY → SERVICE RESPONSE DTO
    // =========================================================

    public ServiceResponseDto toServiceResponseDto(
            ServiceRequest serviceRequest) {

        ServiceResponseDto dto =
                new ServiceResponseDto();

        dto.setId(
                serviceRequest.getId()
        );

        dto.setRequestNumber(
                serviceRequest.getRequestNumber()
        );

        dto.setServiceType(
                serviceRequest.getServiceType()
        );

        dto.setProblemDescription(
                serviceRequest.getProblemDescription()
        );

        dto.setStatus(
                serviceRequest.getStatus()
        );

        dto.setRequestDate(
                serviceRequest.getRequestDate()
        );

        dto.setServiceStartDate(
                serviceRequest.getServiceStartDate()
        );

        dto.setServiceCompletedDate(
                serviceRequest.getServiceCompletedDate()
        );

        dto.setEstimatedCost(
                serviceRequest.getEstimatedCost()
        );

        dto.setActualCost(
                serviceRequest.getActualCost()
        );


        if (serviceRequest.getVehicle() != null) {

            dto.setVehicleId(
                    serviceRequest.getVehicle().getId()
            );

            dto.setVehicleRegistrationNumber(
                    serviceRequest
                            .getVehicle()
                            .getRegistrationNumber()
            );
        }


        if (serviceRequest.getMechanic() != null) {

            dto.setMechanicId(
                    serviceRequest
                            .getMechanic()
                            .getId()
            );

            dto.setMechanicName(
                    serviceRequest
                            .getMechanic()
                            .getName()
            );
        }

        return dto;
    }


    // =========================================================
    // INVOICE ENTITY → INVOICE RESPONSE DTO
    // =========================================================

    public InvoiceResponseDto toInvoiceResponseDto(
            Invoice invoice) {

        InvoiceResponseDto dto =
                new InvoiceResponseDto();

        dto.setId(
                invoice.getId()
        );

        dto.setInvoiceNumber(
                invoice.getInvoiceNumber()
        );

        dto.setServiceCharge(
                invoice.getServiceCharge()
        );

        dto.setPartsCharge(
                invoice.getPartsCharge()
        );

        dto.setTax(
                invoice.getTax()
        );

        dto.setTotalAmount(
                invoice.getTotalAmount()
        );

        dto.setStatus(
                invoice.getStatus()
        );

        dto.setInvoiceDate(
                invoice.getInvoiceDate()
        );

        dto.setPaidDate(
                invoice.getPaidDate()
        );


        if (invoice.getServiceRequest() != null) {

            dto.setServiceRequestId(
                    invoice
                            .getServiceRequest()
                            .getId()
            );

            dto.setRequestNumber(
                    invoice
                            .getServiceRequest()
                            .getRequestNumber()
            );
        }

        return dto;
    }
}