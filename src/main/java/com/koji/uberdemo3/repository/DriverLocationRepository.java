package com.koji.uberdemo3.repository;

import com.koji.uberdemo3.entity.DriverLocation;
import com.koji.uberdemo3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {
    List<DriverLocation> findByDriverAndIsAvailableTrue(User driver);
    
    @Query("SELECT dl FROM DriverLocation dl WHERE dl.isAvailable = true ORDER BY dl.timestamp DESC")
    List<DriverLocation> findLatestAvailableDrivers();
    
    @Query("SELECT dl FROM DriverLocation dl WHERE dl.isAvailable = true AND " +
           "FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :longitude, :latitude), " +
           "FUNCTION('POINT', dl.location.longitude, dl.location.latitude)) <= :radius")
    List<DriverLocation> findNearbyDrivers(@Param("latitude") Double latitude,
                                         @Param("longitude") Double longitude,
                                         @Param("radius") Double radius);
} 