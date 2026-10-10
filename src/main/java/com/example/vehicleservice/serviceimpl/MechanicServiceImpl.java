package com.example.vehicleservice.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vehicleservice.constants.ExceptionConstants;
import com.example.vehicleservice.dto.MechanicRequestDto;
import com.example.vehicleservice.dto.MechanicResponseDto;
import com.example.vehicleservice.entity.Mechanic;
import com.example.vehicleservice.enums.MechanicStatus;
import com.example.vehicleservice.exception.DuplicateResourceException;
import com.example.vehicleservice.exception.ResourceNotFoundException;
import com.example.vehicleservice.mapper.VehicleServiceMapper;
import com.example.vehicleservice.repository.MechanicRepository;
import com.example.vehicleservice.service.MechanicService;

@Service
@Transactional
public class MechanicServiceImpl implements MechanicService {

    private static final Logger logger =
            LoggerFactory.getLogger(MechanicServiceImpl.class);

    private final MechanicRepository mechanicRepository;
    private final VehicleServiceMapper mapper;

    public MechanicServiceImpl(
            MechanicRepository mechanicRepository,
            VehicleServiceMapper mapper) {

        this.mechanicRepository = mechanicRepository;
        this.mapper = mapper;
    }

    // =========================================================
    // CREATE MECHANIC
    // =========================================================

    @Override
    public MechanicResponseDto createMechanic(
            MechanicRequestDto requestDto) {

        logger.info(
                "Creating mechanic with email: {}",
                requestDto.getEmail()
        );

        // Check duplicate email
        if (mechanicRepository.existsByEmail(
                requestDto.getEmail())) {

            logger.warn(
                    "Mechanic email already exists: {}",
                    requestDto.getEmail()
            );

            throw new DuplicateResourceException(
                    ExceptionConstants.MECHANIC_EMAIL_EXISTS
                            + requestDto.getEmail()
            );
        }

        // Check duplicate phone
        if (mechanicRepository.existsByPhone(
                requestDto.getPhone())) {

            logger.warn(
                    "Mechanic phone already exists: {}",
                    requestDto.getPhone()
            );

            throw new DuplicateResourceException(
                    ExceptionConstants.MECHANIC_PHONE_EXISTS
                            + requestDto.getPhone()
            );
        }

        // Create entity
        Mechanic mechanic = new Mechanic();

        mechanic.setName(requestDto.getName());
        mechanic.setPhone(requestDto.getPhone());
        mechanic.setEmail(requestDto.getEmail());
        mechanic.setSpecialization(
                requestDto.getSpecialization()
        );

        // New mechanic is available by default
        mechanic.setStatus(MechanicStatus.AVAILABLE);

        // Save
        Mechanic savedMechanic =
                mechanicRepository.save(mechanic);

        logger.info(
                "Mechanic created successfully with id: {}",
                savedMechanic.getId()
        );

        return mapper.toMechanicResponseDto(savedMechanic);
    }

    // =========================================================
    // GET MECHANIC BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public MechanicResponseDto getMechanicById(Long id) {

        logger.info(
                "Fetching mechanic with id: {}",
                id
        );

        Mechanic mechanic =
                mechanicRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants.MECHANIC_NOT_FOUND
                                                + id
                                )
                        );

        return mapper.toMechanicResponseDto(mechanic);
    }

    // =========================================================
    // GET ALL MECHANICS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<MechanicResponseDto> getAllMechanics(
            Pageable pageable) {

        logger.info(
                "Fetching all mechanics - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Mechanic> mechanicPage =
                mechanicRepository.findAll(pageable);

        return mechanicPage.map(
                mapper::toMechanicResponseDto
        );
    }

    // =========================================================
    // GET MECHANICS BY STATUS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<MechanicResponseDto> getMechanicsByStatus(
            MechanicStatus status,
            Pageable pageable) {

        logger.info(
                "Fetching mechanics with status: {}",
                status
        );

        Page<Mechanic> mechanicPage =
                mechanicRepository.findByStatus(
                        status,
                        pageable
                );

        return mechanicPage.map(
                mapper::toMechanicResponseDto
        );
    }

    // =========================================================
    // GET AVAILABLE MECHANICS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<MechanicResponseDto> getAvailableMechanics(
            Pageable pageable) {

        logger.info("Fetching available mechanics");

        Page<Mechanic> mechanicPage =
                mechanicRepository.findByStatus(
                        MechanicStatus.AVAILABLE,
                        pageable
                );

        return mechanicPage.map(
                mapper::toMechanicResponseDto
        );
    }

    // =========================================================
    // UPDATE MECHANIC
    // =========================================================

    @Override
    public MechanicResponseDto updateMechanic(
            Long id,
            MechanicRequestDto requestDto) {

        logger.info(
                "Updating mechanic with id: {}",
                id
        );

        Mechanic mechanic =
                mechanicRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants.MECHANIC_NOT_FOUND
                                                + id
                                )
                        );

        // Check email only if changed
        if (!mechanic.getEmail()
                .equalsIgnoreCase(requestDto.getEmail())) {

            if (mechanicRepository.existsByEmail(
                    requestDto.getEmail())) {

                throw new DuplicateResourceException(
                        ExceptionConstants.MECHANIC_EMAIL_EXISTS
                                + requestDto.getEmail()
                );
            }

            mechanic.setEmail(requestDto.getEmail());
        }

        // Check phone only if changed
        if (!mechanic.getPhone()
                .equals(requestDto.getPhone())) {

            if (mechanicRepository.existsByPhone(
                    requestDto.getPhone())) {

                throw new DuplicateResourceException(
                        ExceptionConstants.MECHANIC_PHONE_EXISTS
                                + requestDto.getPhone()
                );
            }

            mechanic.setPhone(requestDto.getPhone());
        }

        // Update remaining fields
        mechanic.setName(requestDto.getName());

        mechanic.setSpecialization(
                requestDto.getSpecialization()
        );

        Mechanic updatedMechanic =
                mechanicRepository.save(mechanic);

        logger.info(
                "Mechanic updated successfully with id: {}",
                id
        );

        return mapper.toMechanicResponseDto(
                updatedMechanic
        );
    }

    // =========================================================
    // DELETE MECHANIC
    // =========================================================

    @Override
    public void deleteMechanic(Long id) {

        logger.info(
                "Deleting mechanic with id: {}",
                id
        );

        Mechanic mechanic =
                mechanicRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ExceptionConstants.MECHANIC_NOT_FOUND
                                                + id
                                )
                        );

        mechanicRepository.delete(mechanic);

        logger.info(
                "Mechanic deleted successfully with id: {}",
                id
        );
    }
}