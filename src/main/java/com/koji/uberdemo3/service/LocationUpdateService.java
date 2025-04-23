package com.koji.uberdemo3.service;

import com.koji.uberdemo3.message.LocationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationUpdateService {

    private final RedisGeoService redisGeoService;

    @KafkaListener(topics = "driver-locations", groupId = "uber-group")
    public void handleLocationUpdate(LocationMessage message) {
        log.info("Received location update from Kafka: {}", message);
        
        // Redis GEOに位置情報を更新
        redisGeoService.addDriverLocation(
            message.getDriverId(),
            message.getLatitude(),
            message.getLongitude()
        );
        
        log.info("Updated location in Redis GEO for driver: {}", message.getDriverId());
    }
} 