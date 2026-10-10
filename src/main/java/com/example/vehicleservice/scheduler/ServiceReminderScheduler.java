package com.example.vehicleservice.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.enums.ServiceStatus;
import com.example.vehicleservice.repository.ServiceRequestRepository;

@Component
public class ServiceReminderScheduler {

    private static final Logger logger =
            LoggerFactory.getLogger(ServiceReminderScheduler.class);

    private final ServiceRequestRepository serviceRequestRepository;

    public ServiceReminderScheduler(
            ServiceRequestRepository serviceRequestRepository) {

        this.serviceRequestRepository =
                serviceRequestRepository;
    }

    /**
     * Runs every 5 minutes.
     */
    @Scheduled(fixedRate = 300000)
    public void checkPendingServices() {

        logger.info("Service reminder scheduler started");

        List<ServiceRequest> pendingServices =
                serviceRequestRepository.findByStatus(
                        ServiceStatus.REQUESTED,
                        org.springframework.data.domain.PageRequest.of(0, 100)
                ).getContent();

        if (pendingServices.isEmpty()) {

            logger.info("No pending service requests found");

            return;
        }

        for (ServiceRequest serviceRequest : pendingServices) {

            logger.info(
                "Reminder: Service request {} is still pending for vehicle {}",
                serviceRequest.getRequestNumber(),
                serviceRequest.getVehicle()
                        .getRegistrationNumber()
            );
        }

        logger.info(
            "Pending service requests found: {}",
            pendingServices.size()
        );
    }
}