
package com.example.vehicleservice.serviceimpl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vehicleservice.dto.ServiceRequestPartRequestDto;
import com.example.vehicleservice.entity.Part;
import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.entity.ServiceRequestPart;
import com.example.vehicleservice.repository.PartRepository;
import com.example.vehicleservice.repository.ServiceRequestPartRepository;
import com.example.vehicleservice.repository.ServiceRequestRepository;
import com.example.vehicleservice.service.ServiceRequestPartService;

@Service
public class ServiceRequestPartServiceImpl
        implements ServiceRequestPartService {

    private final ServiceRequestPartRepository serviceRequestPartRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final PartRepository partRepository;

    public ServiceRequestPartServiceImpl(
            ServiceRequestPartRepository serviceRequestPartRepository,
            ServiceRequestRepository serviceRequestRepository,
            PartRepository partRepository) {

        this.serviceRequestPartRepository = serviceRequestPartRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.partRepository = partRepository;
    }

    @Override
    @Transactional
    public ServiceRequestPart addPartToServiceRequest(
            ServiceRequestPartRequestDto request) {

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(
                        request.getServiceRequestId())
                .orElseThrow(() ->
                        new RuntimeException("Service request not found"));

        Part part = partRepository.findById(request.getPartId())
                .orElseThrow(() ->
                        new RuntimeException("Spare part not found"));

        int quantity = request.getQuantity();

        if (part.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Insufficient spare part stock");
        }

        ServiceRequestPart serviceRequestPart =
                new ServiceRequestPart();

        serviceRequestPart.setServiceRequest(serviceRequest);
        serviceRequestPart.setPart(part);
        serviceRequestPart.setQuantity(quantity);

        BigDecimal unitPrice = part.getUnitPrice();

        serviceRequestPart.setUnitPrice(unitPrice);

        BigDecimal totalPrice = unitPrice.multiply(
                BigDecimal.valueOf(quantity));

        serviceRequestPart.setTotalPrice(totalPrice);

        part.setAvailableQuantity(
                part.getAvailableQuantity() - quantity);

        partRepository.save(part);

        return serviceRequestPartRepository.save(serviceRequestPart);
    }
}
