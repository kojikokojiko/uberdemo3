package com.koji.uberdemo3.message;

import lombok.Data;

@Data
public class LocationMessage {
    private Long driverId;
    private double latitude;
    private double longitude;
} 