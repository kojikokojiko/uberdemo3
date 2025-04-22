package com.koji.uberdemo3.dto;

import com.koji.uberdemo3.entity.Location;
import lombok.Data;

@Data
public class RideRequestDTO {
    private Long passengerId;
    private Location pickupLocation;
    private Location dropoffLocation;
    private Double estimatedFare;
} 