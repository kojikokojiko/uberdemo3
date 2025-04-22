package com.koji.uberdemo3.service;

import com.koji.uberdemo3.entity.*;
import com.koji.uberdemo3.repository.RideRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideRequestService {
    private final RideRequestRepository rideRequestRepository;
    private final LocationService locationService;

    @Transactional
    public RideRequest createRideRequest(User passenger, Location pickupLocation, 
                                       Location dropoffLocation, Double estimatedFare) {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setPassenger(passenger);
        rideRequest.setPickupLocation(pickupLocation);
        rideRequest.setDropoffLocation(dropoffLocation);
        rideRequest.setRequestTime(LocalDateTime.now());
        rideRequest.setStatus(RideRequest.RideStatus.PENDING);
        rideRequest.setEstimatedFare(estimatedFare);
        
        return rideRequestRepository.save(rideRequest);
    }

    @Transactional
    public RideRequest acceptRideRequest(Long requestId, User driver) {
        RideRequest rideRequest = rideRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Ride request not found"));
        
        if (rideRequest.getStatus() != RideRequest.RideStatus.PENDING) {
            throw new RuntimeException("Ride request is not in PENDING status");
        }
        
        rideRequest.setDriver(driver);
        rideRequest.setStatus(RideRequest.RideStatus.ACCEPTED);
        rideRequest.setAcceptedTime(LocalDateTime.now());
        
        return rideRequestRepository.save(rideRequest);
    }

    @Transactional
    public RideRequest updateRideStatus(Long requestId, RideRequest.RideStatus newStatus) {
        RideRequest rideRequest = rideRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Ride request not found"));
        
        rideRequest.setStatus(newStatus);
        if (newStatus == RideRequest.RideStatus.COMPLETED) {
            rideRequest.setCompletedTime(LocalDateTime.now());
        }
        
        return rideRequestRepository.save(rideRequest);
    }

    public List<RideRequest> getPendingRequests() {
        return rideRequestRepository.findPendingRequests();
    }

    public List<RideRequest> getActiveRidesByDriver(User driver) {
        return rideRequestRepository.findActiveRidesByDriver(driver);
    }

    public List<RideRequest> getActiveRidesByPassenger(User passenger) {
        return rideRequestRepository.findActiveRidesByPassenger(passenger);
    }

    public List<RideRequest> getRideHistoryByUser(User user) {
        return rideRequestRepository.findByPassenger(user);
    }
} 