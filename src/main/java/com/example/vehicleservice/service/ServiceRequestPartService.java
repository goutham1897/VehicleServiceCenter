
package com.example.vehicleservice.service;

import com.example.vehicleservice.dto.ServiceRequestPartRequestDto;
import com.example.vehicleservice.entity.ServiceRequestPart;

public interface ServiceRequestPartService {

    ServiceRequestPart addPartToServiceRequest(
            ServiceRequestPartRequestDto request);
}

