package com.koji.uberdemo3.repository;

import com.koji.uberdemo3.entity.DriverLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {
    List<DriverLocation> findByDriverIdAndIsAvailableTrue(Long driverId);
    
    @Query("SELECT dl FROM DriverLocation dl WHERE dl.isAvailable = true ORDER BY dl.timestamp DESC")
    List<DriverLocation> findLatestAvailableDrivers();
    
    @Query("SELECT dl FROM DriverLocation dl WHERE " +
           "6371 * acos(cos(radians(:latitude)) * cos(radians(dl.latitude)) * " +
           "cos(radians(dl.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(dl.latitude))) <= :radius")
    List<DriverLocation> findNearbyDrivers(@Param("latitude") Double latitude,
                                         @Param("longitude") Double longitude,
                                         @Param("radius") Double radius);
} 