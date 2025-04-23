package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.entity.DriverLocation;
import com.koji.uberdemo3.entity.Location;
import com.koji.uberdemo3.entity.User;
import com.koji.uberdemo3.message.LocationMessage;
import com.koji.uberdemo3.service.KafkaProducerService;
import com.koji.uberdemo3.service.LocationService;
import com.koji.uberdemo3.service.RedisGeoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;
    private final KafkaProducerService kafkaProducerService;
    private final RedisGeoService redisGeoService;

    @PostMapping("/driver")
    public ResponseEntity<Void> updateDriverLocation(@RequestBody LocationMessage message) {
        log.info("Received location update request: {}", message);
        
        // Kafkaにメッセージを送信
        kafkaProducerService.sendLocationUpdate(message);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<DriverLocation>> findNearbyDrivers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5") double radius) {
        
        log.info("Finding nearby drivers - lat: {}, lon: {}, radius: {}", latitude, longitude, radius);
        
        // Redis GEOから近くのドライバーを検索
        List<DriverLocation> nearbyDrivers = redisGeoService.findNearbyDrivers(latitude, longitude, radius);
        
        return ResponseEntity.ok(nearbyDrivers);
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverLocation>> getAvailableDrivers() {
        List<DriverLocation> availableDrivers = locationService.getAvailableDrivers();
        return ResponseEntity.ok(availableDrivers);
    }
} 