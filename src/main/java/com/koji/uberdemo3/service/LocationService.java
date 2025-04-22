package com.koji.uberdemo3.service;

import com.koji.uberdemo3.entity.DriverLocation;
import com.koji.uberdemo3.entity.Location;
import com.koji.uberdemo3.entity.User;
import com.koji.uberdemo3.repository.DriverLocationRepository;
import com.koji.uberdemo3.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final DriverLocationRepository driverLocationRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public DriverLocation updateDriverLocation(User driver, Location location, boolean isAvailable) {
        DriverLocation driverLocation = new DriverLocation();
        driverLocation.setDriver(driver);
        driverLocation.setLocation(location);
        driverLocation.setTimestamp(LocalDateTime.now());
        driverLocation.setIsAvailable(isAvailable);
        
        return driverLocationRepository.save(driverLocation);
    }

    public List<DriverLocation> findNearbyDrivers(Double latitude, Double longitude, Double radius) {
        return driverLocationRepository.findNearbyDrivers(latitude, longitude, radius);
    }

    public List<DriverLocation> getAvailableDrivers() {
        return driverLocationRepository.findLatestAvailableDrivers();
    }
} 