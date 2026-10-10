package com.example.vehicleservice.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vehicleservice.constants.ExceptionConstants;
import com.example.vehicleservice.dto.ServiceRequestDto;
import com.example.vehicleservice.dto.ServiceResponseDto;
import com.example.vehicleservice.entity.Mechanic;
import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.enums.MechanicStatus;
import com.example.vehicleservice.enums.ServiceStatus;
import com.example.vehicleservice.exception.BusinessException;
import com.example.vehicleservice.exception.ResourceNotFoundException;
import com.example.vehicleservice.mapper.VehicleServiceMapper;
import com.example.vehicleservice.repository.MechanicRepository;
import com.example.vehicleservice.repository.ServiceRequestRepository;
import com.example.vehicleservice.repository.VehicleRepository;
import com.example.vehicleservice.service.ServiceRequestService;

@Service
@Transactional
public class ServiceRequestServiceImpl
        implements ServiceRequestService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    ServiceRequestServiceImpl.class);

    private final ServiceRequestRepository serviceRequestRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final VehicleServiceMapper mapper;

    public ServiceRequestServiceImpl(
            ServiceRequestRepository serviceRequestRepository,
            VehicleRepository vehicleRepository,
            MechanicRepository mechanicRepository,
            VehicleServiceMapper mapper) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.vehicleRepository = vehicleRepository;
        this.mechanicRepository = mechanicRepository;
        this.mapper = mapper;
    }

    // =========================================================
    // CREATE SERVICE REQUEST
    // =========================================================

    @Override
    public ServiceResponseDto createServiceRequest(
            ServiceRequestDto requestDto) {

        logger.info(
                "Creating service request for vehicle id: {}",
                requestDto.getVehicleId());

        // Find vehicle
        Vehicle vehicle = vehicleRepository.findById(
                requestDto.getVehicleId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        ExceptionConstants.VEHICLE_NOT_FOUND
                                + requestDto.getVehicleId()));

        // Create service request
        ServiceRequest serviceRequest =
                new ServiceRequest();

        serviceRequest.setVehicle(vehicle);

        serviceRequest.setServiceType(
                requestDto.getServiceType());

        serviceRequest.setProblemDescription(
                requestDto.getProblemDescription());

        serviceRequest.setEstimatedCost(
                requestDto.getEstimatedCost());

        serviceRequest.setStatus(
                ServiceStatus.REQUESTED);

        serviceRequest.setRequestDate(
                LocalDateTime.now());

        // Generate request number
        String requestNumber =
                generateRequestNumber();

        serviceRequest.setRequestNumber(
                requestNumber);

        // Optional mechanic assignment
        if (requestDto.getMechanicId() != null) {

            Mechanic mechanic =
                    mechanicRepository.findById(
                            requestDto.getMechanicId()
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(
                                    ExceptionConstants
                                            .MECHANIC_NOT_FOUND
                                            + requestDto
                                            .getMechanicId()));

            if (mechanic.getStatus()
                    != MechanicStatus.AVAILABLE) {

                throw new BusinessException(
                        ExceptionConstants
                                .MECHANIC_NOT_AVAILABLE);
            }

            serviceRequest.setMechanic(
                    mechanic);

            serviceRequest.setStatus(
                    ServiceStatus.ASSIGNED);

            mechanic.setStatus(
                    MechanicStatus.BUSY);

            mechanicRepository.save(mechanic);
        }

        ServiceRequest savedRequest =
                serviceRequestRepository.save(
                        serviceRequest);

        logger.info(
                "Service request created successfully: {}",
                savedRequest.getRequestNumber());

        return mapper.toServiceResponseDto(
                savedRequest);
    }

    // =========================================================
    // GENERATE REQUEST NUMBER
    // =========================================================

    private String generateRequestNumber() {

        return "SR-" +
                System.currentTimeMillis();
    }

    // =========================================================
    // GET SERVICE REQUEST BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public ServiceResponseDto getServiceRequestById(
            Long id) {

        logger.info(
                "Fetching service request id: {}",
                id);

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants
                                                .SERVICE_REQUEST_NOT_FOUND
                                                + id));

        return mapper.toServiceResponseDto(
                serviceRequest);
    }

    // =========================================================
    // GET ALL SERVICE REQUESTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceResponseDto> getAllServiceRequests(
            Pageable pageable) {

        logger.info(
                "Fetching service requests - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        Page<ServiceRequest> requests =
                serviceRequestRepository.findAll(
                        pageable);

        return requests.map(
                mapper::toServiceResponseDto);
    }

    // =========================================================
    // GET SERVICE REQUESTS BY STATUS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceResponseDto> getServiceRequestsByStatus(
            ServiceStatus status,
            Pageable pageable) {

        logger.info(
                "Fetching service requests with status: {}",
                status);

        Page<ServiceRequest> requests =
                serviceRequestRepository.findByStatus(
                        status,
                        pageable);

        return requests.map(
                mapper::toServiceResponseDto);
    }

    // =========================================================
    // SERVICE HISTORY BY VEHICLE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceResponseDto> getServiceHistoryByVehicle(
            Long vehicleId,
            Pageable pageable) {

        logger.info(
                "Fetching service history for vehicle id: {}",
                vehicleId);

        if (!vehicleRepository.existsById(vehicleId)) {

            throw new ResourceNotFoundException(
                    ExceptionConstants.VEHICLE_NOT_FOUND
                            + vehicleId);
        }

        Page<ServiceRequest> requests =
                serviceRequestRepository
                        .findServiceHistoryByVehicle(
                                vehicleId,
                                pageable);

        return requests.map(
                mapper::toServiceResponseDto);
    }

    // =========================================================
    // SERVICE HISTORY BY CUSTOMER
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponseDto> getServiceHistoryByCustomer(
            Long customerId) {

        logger.info(
                "Fetching service history for customer id: {}",
                customerId);

        List<ServiceRequest> requests =
                serviceRequestRepository
                        .findServiceHistoryByCustomer(
                                customerId);

        return requests.stream()
                .map(mapper::toServiceResponseDto)
                .toList();
    }

    // =========================================================
    // JOBS BY MECHANIC
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponseDto> getJobsByMechanic(
            Long mechanicId) {

        logger.info(
                "Fetching jobs for mechanic id: {}",
                mechanicId);

        if (!mechanicRepository.existsById(
                mechanicId)) {

            throw new ResourceNotFoundException(
                    ExceptionConstants.MECHANIC_NOT_FOUND
                            + mechanicId);
        }

        List<ServiceRequest> requests =
                serviceRequestRepository
                        .findJobsByMechanic(
                                mechanicId);

        return requests.stream()
                .map(mapper::toServiceResponseDto)
                .toList();
    }

    // =========================================================
    // UPDATE SERVICE REQUEST
    // =========================================================

    @Override
    public ServiceResponseDto updateServiceRequest(
            Long id,
            ServiceRequestDto requestDto) {

        logger.info(
                "Updating service request id: {}",
                id);

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants
                                                .SERVICE_REQUEST_NOT_FOUND
                                                + id));

        Vehicle vehicle =
                vehicleRepository.findById(
                        requestDto.getVehicleId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants
                                        .VEHICLE_NOT_FOUND
                                        + requestDto
                                        .getVehicleId()));

        serviceRequest.setVehicle(vehicle);

        serviceRequest.setServiceType(
                requestDto.getServiceType());

        serviceRequest.setProblemDescription(
                requestDto.getProblemDescription());

        serviceRequest.setEstimatedCost(
                requestDto.getEstimatedCost());

        // Update mechanic only when supplied
        if (requestDto.getMechanicId() != null) {

            assignMechanicInternal(
                    serviceRequest,
                    requestDto.getMechanicId());
        }

        ServiceRequest updatedRequest =
                serviceRequestRepository.save(
                        serviceRequest);

        logger.info(
                "Service request updated successfully: {}",
                id);

        return mapper.toServiceResponseDto(
                updatedRequest);
    }

    // =========================================================
    // ASSIGN MECHANIC
    // =========================================================

    @Override
    public ServiceResponseDto assignMechanic(
            Long serviceRequestId,
            Long mechanicId) {

        logger.info(
                "Assigning mechanic {} to service request {}",
                mechanicId,
                serviceRequestId);

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(
                        serviceRequestId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants
                                        .SERVICE_REQUEST_NOT_FOUND
                                        + serviceRequestId));

        assignMechanicInternal(
                serviceRequest,
                mechanicId);

        ServiceRequest updatedRequest =
                serviceRequestRepository.save(
                        serviceRequest);

        return mapper.toServiceResponseDto(
                updatedRequest);
    }

    // =========================================================
    // INTERNAL MECHANIC ASSIGNMENT
    // =========================================================

    private void assignMechanicInternal(
            ServiceRequest serviceRequest,
            Long mechanicId) {

        Mechanic mechanic =
                mechanicRepository.findById(
                        mechanicId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants
                                        .MECHANIC_NOT_FOUND
                                        + mechanicId));

        if (mechanic.getStatus()
                != MechanicStatus.AVAILABLE) {

            throw new BusinessException(
                    ExceptionConstants
                            .MECHANIC_NOT_AVAILABLE);
        }

        serviceRequest.setMechanic(
                mechanic);

        serviceRequest.setStatus(
                ServiceStatus.ASSIGNED);

        mechanic.setStatus(
                MechanicStatus.BUSY);

        mechanicRepository.save(mechanic);
    }

    // =========================================================
    // UPDATE JOB STATUS
    // =========================================================

    @Override
    public ServiceResponseDto updateJobStatus(
            Long serviceRequestId,
            ServiceStatus newStatus) {

        logger.info(
                "Updating job {} to status {}",
                serviceRequestId,
                newStatus);

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(
                        serviceRequestId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants
                                        .SERVICE_REQUEST_NOT_FOUND
                                        + serviceRequestId));

        ServiceStatus currentStatus =
                serviceRequest.getStatus();

        // =====================================================
        // CANCELLED
        // =====================================================

        if (newStatus == ServiceStatus.CANCELLED) {

            if (currentStatus ==
                    ServiceStatus.COMPLETED) {

                throw new BusinessException(
                        "Completed service cannot be cancelled");
            }

            releaseMechanic(serviceRequest);

            serviceRequest.setStatus(
                    ServiceStatus.CANCELLED);

            return mapper.toServiceResponseDto(
                    serviceRequestRepository.save(
                            serviceRequest));
        }

        // =====================================================
        // IN_PROGRESS
        // =====================================================

        if (newStatus ==
                ServiceStatus.IN_PROGRESS) {

            if (serviceRequest.getMechanic()
                    == null) {

                throw new BusinessException(
                        "Mechanic must be assigned before starting service");
            }

            if (currentStatus !=
                    ServiceStatus.ASSIGNED) {

                throw new BusinessException(
                        "Only an assigned service can be started");
            }

            serviceRequest.setStatus(
                    ServiceStatus.IN_PROGRESS);

            serviceRequest.setServiceStartDate(
                    LocalDateTime.now());
        }

        // =====================================================
        // WAITING FOR PARTS
        // =====================================================

        else if (newStatus ==
                ServiceStatus.WAITING_FOR_PARTS) {

            if (currentStatus !=
                    ServiceStatus.IN_PROGRESS) {

                throw new BusinessException(
                        "Service must be in progress before waiting for parts");
            }

            serviceRequest.setStatus(
                    ServiceStatus.WAITING_FOR_PARTS);
        }

        // =====================================================
        // COMPLETED
        // =====================================================

        else if (newStatus ==
                ServiceStatus.COMPLETED) {

            /*
             * Service can be completed only when it
             * is currently IN_PROGRESS.
             */
            if (currentStatus !=
                    ServiceStatus.IN_PROGRESS) {

                throw new BusinessException(
                        "Only an in-progress service can be completed");
            }

            // Set completed status
            serviceRequest.setStatus(
                    ServiceStatus.COMPLETED);

            // Set completed date
            serviceRequest.setServiceCompletedDate(
                    LocalDateTime.now());

            // =================================================
            // SET ACTUAL COST
            // =================================================

            /*
             * For now actual cost is taken from estimated cost.
             *
             * Later, when parts are allocated, we can calculate:
             *
             * actualCost =
             * serviceCharge + partsCharge
             */
            if (serviceRequest.getEstimatedCost() != null) {

                serviceRequest.setActualCost(
                        serviceRequest.getEstimatedCost());

            } else {

                serviceRequest.setActualCost(
                        BigDecimal.ZERO);
            }

            // Release mechanic
            releaseMechanic(serviceRequest);
        }

        // =====================================================
        // APPROVED
        // =====================================================

        else if (newStatus ==
                ServiceStatus.APPROVED) {

            if (currentStatus !=
                    ServiceStatus.REQUESTED) {

                throw new BusinessException(
                        "Only a requested service can be approved");
            }

            serviceRequest.setStatus(
                    ServiceStatus.APPROVED);
        }

        // =====================================================
        // ASSIGNED
        // =====================================================

        else if (newStatus ==
                ServiceStatus.ASSIGNED) {

            if (serviceRequest.getMechanic()
                    == null) {

                throw new BusinessException(
                        "Mechanic must be assigned");
            }

            serviceRequest.setStatus(
                    ServiceStatus.ASSIGNED);
        }

        // =====================================================
        // INVALID STATUS
        // =====================================================

        else {

            throw new BusinessException(
                    ExceptionConstants.INVALID_SERVICE_STATUS);
        }

        // =====================================================
        // SAVE
        // =====================================================

        ServiceRequest updatedRequest =
                serviceRequestRepository.save(
                        serviceRequest);

        logger.info(
                "Service request status updated successfully. " +
                "ID: {}, Status: {}, Actual Cost: {}, Completed Date: {}",
                updatedRequest.getId(),
                updatedRequest.getStatus(),
                updatedRequest.getActualCost(),
                updatedRequest.getServiceCompletedDate());

        return mapper.toServiceResponseDto(
                updatedRequest);
    }

    // =========================================================
    // RELEASE MECHANIC
    // =========================================================

    private void releaseMechanic(
            ServiceRequest serviceRequest) {

        if (serviceRequest.getMechanic() != null) {

            Mechanic mechanic =
                    serviceRequest.getMechanic();

            mechanic.setStatus(
                    MechanicStatus.AVAILABLE);

            mechanicRepository.save(mechanic);
        }
    }

    // =========================================================
    // CANCEL SERVICE REQUEST
    // =========================================================

    @Override
    public ServiceResponseDto cancelServiceRequest(
            Long serviceRequestId) {

        logger.info(
                "Cancelling service request id: {}",
                serviceRequestId);

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(
                        serviceRequestId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants
                                        .SERVICE_REQUEST_NOT_FOUND
                                        + serviceRequestId));

        if (serviceRequest.getStatus() ==
                ServiceStatus.COMPLETED) {

            throw new BusinessException(
                    "Completed service cannot be cancelled");
        }

        releaseMechanic(serviceRequest);

        serviceRequest.setStatus(
                ServiceStatus.CANCELLED);

        ServiceRequest cancelledRequest =
                serviceRequestRepository.save(
                        serviceRequest);

        logger.info(
                "Service request cancelled successfully");

        return mapper.toServiceResponseDto(
                cancelledRequest);
    }
}