
package com.example.vehicleservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.vehicleservice.entity.ServiceRequestPart;

public interface ServiceRequestPartRepository
        extends JpaRepository<ServiceRequestPart, Long> {
}
