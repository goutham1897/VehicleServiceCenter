package com.example.vehicleservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vehicleservice.entity.ServiceRequest;
import com.example.vehicleservice.enums.ServiceStatus;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {

    Optional<ServiceRequest> findByRequestNumber(
            String requestNumber);

    boolean existsByRequestNumber(
            String requestNumber);

    Page<ServiceRequest> findByStatus(
            ServiceStatus status,
            Pageable pageable);

    Page<ServiceRequest> findByVehicleId(
            Long vehicleId,
            Pageable pageable);

    // =====================================================
    // JPQL - SERVICE HISTORY BY VEHICLE
    // =====================================================

    @Query("""
        SELECT s
        FROM ServiceRequest s
        WHERE s.vehicle.id = :vehicleId
        ORDER BY s.requestDate DESC
    """)
    Page<ServiceRequest> findServiceHistoryByVehicle(
            @Param("vehicleId") Long vehicleId,
            Pageable pageable);

    // =====================================================
    // JPQL - SERVICE HISTORY BY CUSTOMER
    // =====================================================

    @Query("""
        SELECT s
        FROM ServiceRequest s
        WHERE s.vehicle.customer.id = :customerId
        ORDER BY s.requestDate DESC
    """)
    List<ServiceRequest> findServiceHistoryByCustomer(
            @Param("customerId") Long customerId);

    // =====================================================
    // JPQL - JOBS BY MECHANIC
    // =====================================================

    @Query("""
        SELECT s
        FROM ServiceRequest s
        WHERE s.mechanic.id = :mechanicId
        ORDER BY s.requestDate DESC
    """)
    List<ServiceRequest> findJobsByMechanic(
            @Param("mechanicId") Long mechanicId);

    // =====================================================
    // NATIVE QUERY - STATUS
    // =====================================================

    @Query(
        value = """
            SELECT *
            FROM service_requests
            WHERE status = :status
        """,
        nativeQuery = true
    )
    List<ServiceRequest> findByStatusNative(
            @Param("status") String status);

    // =====================================================
    // NATIVE QUERY - CUSTOMER HISTORY
    // =====================================================

    @Query(
        value = """
            SELECT sr.*
            FROM service_requests sr
            JOIN vehicles v
                ON sr.vehicle_id = v.id
            WHERE v.customer_id = :customerId
            ORDER BY sr.request_date DESC
        """,
        nativeQuery = true
    )
    List<ServiceRequest> findCustomerHistoryNative(
            @Param("customerId") Long customerId);

    // =====================================================
    // JPQL - ACTIVE JOB COUNT
    // =====================================================

    @Query("""
        SELECT COUNT(s)
        FROM ServiceRequest s
        WHERE s.mechanic.id = :mechanicId
        AND s.status IN (
            com.example.vehicleservice.enums.ServiceStatus.ASSIGNED,
            com.example.vehicleservice.enums.ServiceStatus.IN_PROGRESS,
            com.example.vehicleservice.enums.ServiceStatus.WAITING_FOR_PARTS
        )
    """)
    long countActiveJobsByMechanic(
            @Param("mechanicId") Long mechanicId);
}