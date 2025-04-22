package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.dto.*;
import com.koji.uberdemo3.entity.Location;
import com.koji.uberdemo3.service.FareCalculationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fare")
@RequiredArgsConstructor
@Validated
public class FareController {

    private final FareCalculationService fareCalculationService;

    @PostMapping("/calculate")
    public ResponseEntity<FareResponse> calculateFare(
            @Valid @RequestBody FareRequest request,
            @RequestParam(defaultValue = "1.0") @DecimalMin("1.0") @DecimalMax("5.0") double surgeMultiplier) {
        
        double fare = fareCalculationService.calculateFare(
                request.getPickupLocation(),
                request.getDropoffLocation(),
                surgeMultiplier
        );

        double distance = fareCalculationService.calculateDistance(
                request.getPickupLocation(),
                request.getDropoffLocation()
        );

        double duration = fareCalculationService.calculateDuration(
                request.getPickupLocation(),
                request.getDropoffLocation()
        );

        FareResponse response = new FareResponse(fare, distance, duration, surgeMultiplier);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/distance")
    public ResponseEntity<DistanceResponse> calculateDistance(
            @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") double lat1,
            @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") double lon1,
            @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") double lat2,
            @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") double lon2) {
        
        Location location1 = new Location();
        location1.setLatitude(lat1);
        location1.setLongitude(lon1);

        Location location2 = new Location();
        location2.setLatitude(lat2);
        location2.setLongitude(lon2);

        double distance = fareCalculationService.calculateDistance(location1, location2);
        return ResponseEntity.ok(new DistanceResponse(distance));
    }

    @GetMapping("/duration")
    public ResponseEntity<DurationResponse> calculateDuration(
            @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") double lat1,
            @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") double lon1,
            @RequestParam @DecimalMin("-90.0") @DecimalMax("90.0") double lat2,
            @RequestParam @DecimalMin("-180.0") @DecimalMax("180.0") double lon2) {
        
        Location location1 = new Location();
        location1.setLatitude(lat1);
        location1.setLongitude(lon1);

        Location location2 = new Location();
        location2.setLatitude(lat2);
        location2.setLongitude(lon2);

        double duration = fareCalculationService.calculateDuration(location1, location2);
        return ResponseEntity.ok(new DurationResponse(duration));
    }
} 