package com.koji.uberdemo3.repository;

import com.koji.uberdemo3.entity.RideRequest;
import com.koji.uberdemo3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
    List<RideRequest> findByPassenger(User passenger);
    List<RideRequest> findByDriver(User driver);
    
    @Query("SELECT r FROM RideRequest r WHERE r.status = 'PENDING' ORDER BY r.requestTime DESC")
    List<RideRequest> findPendingRequests();
    
    @Query("SELECT r FROM RideRequest r WHERE r.driver = :driver AND r.status IN ('ACCEPTED', 'IN_PROGRESS')")
    List<RideRequest> findActiveRidesByDriver(@Param("driver") User driver);
    
    @Query("SELECT r FROM RideRequest r WHERE r.passenger = :passenger AND r.status IN ('ACCEPTED', 'IN_PROGRESS')")
    List<RideRequest> findActiveRidesByPassenger(@Param("passenger") User passenger);
} 