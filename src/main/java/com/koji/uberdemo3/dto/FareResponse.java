package com.koji.uberdemo3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FareResponse {
    private double fare;
    private double distance;
    private double duration;
    private double surgeMultiplier;
} 