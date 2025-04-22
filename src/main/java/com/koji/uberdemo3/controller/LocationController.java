package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.entity.DriverLocation;
import com.koji.uberdemo3.entity.Location;
import com.koji.uberdemo3.entity.User;
import com.koji.uberdemo3.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/driver/{driverId}")
    public ResponseEntity<DriverLocation> updateDriverLocation(
            @PathVariable Long driverId,
            @RequestBody Location location,
            @RequestParam boolean isAvailable) {
        // TODO: Get actual driver from authentication
        User driver = new User();
        driver.setId(driverId);
        
        DriverLocation updatedLocation = locationService.updateDriverLocation(driver, location, isAvailable);
        return ResponseEntity.ok(updatedLocation);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<DriverLocation>> findNearbyDrivers(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5000") Double radius) {
        List<DriverLocation> nearbyDrivers = locationService.findNearbyDrivers(latitude, longitude, radius);
        return ResponseEntity.ok(nearbyDrivers);
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverLocation>> getAvailableDrivers() {
        List<DriverLocation> availableDrivers = locationService.getAvailableDrivers();
        return ResponseEntity.ok(availableDrivers);
    }
} 