package com.example.vehicleservice.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.vehicleservice.constants.ExceptionConstants;
import com.example.vehicleservice.dto.PartRequestDto;
import com.example.vehicleservice.dto.PartResponseDto;
import com.example.vehicleservice.entity.Part;
import com.example.vehicleservice.exception.DuplicateResourceException;
import com.example.vehicleservice.exception.ResourceNotFoundException;
import com.example.vehicleservice.exception.BusinessException;
import com.example.vehicleservice.mapper.VehicleServiceMapper;
import com.example.vehicleservice.repository.PartRepository;
import com.example.vehicleservice.service.PartService;

@Service
@Transactional
public class PartServiceImpl implements PartService {

    private static final Logger logger =
            LoggerFactory.getLogger(PartServiceImpl.class);

    private final PartRepository partRepository;
    private final VehicleServiceMapper mapper;

    public PartServiceImpl(
            PartRepository partRepository,
            VehicleServiceMapper mapper) {

        this.partRepository = partRepository;
        this.mapper = mapper;
    }

    // =========================================================
    // CREATE PART
    // =========================================================

    @Override
    public PartResponseDto createPart(
            PartRequestDto requestDto) {

        logger.info(
                "Creating part with part number: {}",
                requestDto.getPartNumber()
        );

        // Check duplicate part number
        if (partRepository.existsByPartNumber(
                requestDto.getPartNumber())) {

            logger.warn(
                    "Part number already exists: {}",
                    requestDto.getPartNumber()
            );

            throw new DuplicateResourceException(
                    ExceptionConstants.PART_NUMBER_EXISTS
                            + requestDto.getPartNumber()
            );
        }

        // Create entity
        Part part = new Part();

        part.setPartName(requestDto.getPartName());
        part.setPartNumber(requestDto.getPartNumber());
        part.setAvailableQuantity(
                requestDto.getAvailableQuantity()
        );
        part.setUnitPrice(requestDto.getUnitPrice());
        part.setManufacturer(
                requestDto.getManufacturer()
        );

        // Save
        Part savedPart =
                partRepository.save(part);

        logger.info(
                "Part created successfully with id: {}",
                savedPart.getId()
        );

        return mapper.toPartResponseDto(savedPart);
    }

    // =========================================================
    // GET PART BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PartResponseDto getPartById(Long id) {

        logger.info(
                "Fetching part with id: {}",
                id
        );

        Part part = partRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants.PART_NOT_FOUND
                                        + id
                        )
                );

        return mapper.toPartResponseDto(part);
    }

    // =========================================================
    // GET ALL PARTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<PartResponseDto> getAllParts(
            Pageable pageable) {

        logger.info(
                "Fetching all parts - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Part> partPage =
                partRepository.findAll(pageable);

        return partPage.map(
                mapper::toPartResponseDto
        );
    }

    // =========================================================
    // GET AVAILABLE PARTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<PartResponseDto> getAvailableParts(
            Pageable pageable) {

        logger.info("Fetching available parts");

        Page<Part> partPage =
                partRepository
                        .findByAvailableQuantityGreaterThan(
                                0,
                                pageable
                        );

        return partPage.map(
                mapper::toPartResponseDto
        );
    }

    // =========================================================
    // UPDATE PART
    // =========================================================

    @Override
    public PartResponseDto updatePart(
            Long id,
            PartRequestDto requestDto) {

        logger.info(
                "Updating part with id: {}",
                id
        );

        Part part = partRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants.PART_NOT_FOUND
                                        + id
                        )
                );

        // Check part number only if changed
        if (!part.getPartNumber()
                .equalsIgnoreCase(requestDto.getPartNumber())) {

            if (partRepository.existsByPartNumber(
                    requestDto.getPartNumber())) {

                throw new DuplicateResourceException(
                        ExceptionConstants.PART_NUMBER_EXISTS
                                + requestDto.getPartNumber()
                );
            }

            part.setPartNumber(
                    requestDto.getPartNumber()
            );
        }

        // Update fields
        part.setPartName(
                requestDto.getPartName()
        );

        part.setAvailableQuantity(
                requestDto.getAvailableQuantity()
        );

        part.setUnitPrice(
                requestDto.getUnitPrice()
        );

        part.setManufacturer(
                requestDto.getManufacturer()
        );

        Part updatedPart =
                partRepository.save(part);

        logger.info(
                "Part updated successfully with id: {}",
                id
        );

        return mapper.toPartResponseDto(
                updatedPart
        );
    }

    // =========================================================
    // DELETE PART
    // =========================================================

    @Override
    public void deletePart(Long id) {

        logger.info(
                "Deleting part with id: {}",
                id
        );

        Part part = partRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants.PART_NOT_FOUND
                                        + id
                        )
                );

        partRepository.delete(part);

        logger.info(
                "Part deleted successfully with id: {}",
                id
        );
    }

    // =========================================================
    // REDUCE PART STOCK
    // =========================================================

    @Override
    public void reduceStock(
            Long partId,
            Integer quantity) {

        logger.info(
                "Reducing stock for part id: {}, quantity: {}",
                partId,
                quantity
        );

        // Validate quantity
        if (quantity == null || quantity <= 0) {

            throw new BusinessException(
                    "Quantity must be greater than zero"
            );
        }

        // Find part
        Part part = partRepository.findById(partId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ExceptionConstants.PART_NOT_FOUND
                                        + partId
                        )
                );

        // Check stock
        if (part.getAvailableQuantity() < quantity) {

            logger.warn(
                    "Insufficient stock for part id: {}",
                    partId
            );

            throw new BusinessException(
                    ExceptionConstants.INSUFFICIENT_PART_STOCK
                            + partId
            );
        }

        // Reduce stock
        int remainingQuantity =
                part.getAvailableQuantity() - quantity;

        part.setAvailableQuantity(
                remainingQuantity
        );

        partRepository.save(part);

        logger.info(
                "Stock updated successfully. Remaining quantity: {}",
                remainingQuantity
        );
    }
}