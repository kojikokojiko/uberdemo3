package com.koji.uberdemo3.dto;

import com.koji.uberdemo3.entity.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FareRequest {
    @Valid
    @NotNull(message = "乗車地点は必須です")
    private Location pickupLocation;

    @Valid
    @NotNull(message = "降車地点は必須です")
    private Location dropoffLocation;
} 