package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.dto.RideRequestDTO;
import com.koji.uberdemo3.entity.*;
import com.koji.uberdemo3.service.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideRequestController {
    private final RideRequestService rideRequestService;

    @PostMapping("/request")
    public ResponseEntity<RideRequest> createRideRequest(
            @RequestBody RideRequestDTO requestDTO) {
        // TODO: Get actual user from authentication
        User passenger = new User();
        passenger.setId(requestDTO.getPassengerId());
        
        RideRequest rideRequest = rideRequestService.createRideRequest(
                passenger,
                requestDTO.getPickupLocation(),
                requestDTO.getDropoffLocation(),
                requestDTO.getEstimatedFare()
        );
        
        return ResponseEntity.ok(rideRequest);
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<RideRequest> acceptRideRequest(
            @PathVariable Long requestId,
            @RequestParam Long driverId) {
        // TODO: Get actual driver from authentication
        User driver = new User();
        driver.setId(driverId);
        
        RideRequest rideRequest = rideRequestService.acceptRideRequest(requestId, driver);
        return ResponseEntity.ok(rideRequest);
    }

    @PutMapping("/{requestId}/status")
    public ResponseEntity<RideRequest> updateRideStatus(
            @PathVariable Long requestId,
            @RequestParam RideRequest.RideStatus status) {
        RideRequest rideRequest = rideRequestService.updateRideStatus(requestId, status);
        return ResponseEntity.ok(rideRequest);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<RideRequest>> getPendingRequests() {
        List<RideRequest> pendingRequests = rideRequestService.getPendingRequests();
        return ResponseEntity.ok(pendingRequests);
    }

    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<List<RideRequest>> getActiveRidesByDriver(
            @PathVariable Long driverId) {
        // TODO: Get actual driver from authentication
        User driver = new User();
        driver.setId(driverId);
        
        List<RideRequest> activeRides = rideRequestService.getActiveRidesByDriver(driver);
        return ResponseEntity.ok(activeRides);
    }

    @GetMapping("/passenger/{passengerId}/active")
    public ResponseEntity<List<RideRequest>> getActiveRidesByPassenger(
            @PathVariable Long passengerId) {
        // TODO: Get actual passenger from authentication
        User passenger = new User();
        passenger.setId(passengerId);
        
        List<RideRequest> activeRides = rideRequestService.getActiveRidesByPassenger(passenger);
        return ResponseEntity.ok(activeRides);
    }

    @GetMapping("/passenger/{passengerId}/history")
    public ResponseEntity<List<RideRequest>> getRideHistory(
            @PathVariable Long passengerId) {
        // TODO: Get actual passenger from authentication
        User passenger = new User();
        passenger.setId(passengerId);
        
        List<RideRequest> rideHistory = rideRequestService.getRideHistoryByUser(passenger);
        return ResponseEntity.ok(rideHistory);
    }
} 