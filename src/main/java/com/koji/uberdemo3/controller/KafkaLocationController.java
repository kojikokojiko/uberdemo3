package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.message.LocationMessage;
import com.koji.uberdemo3.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaLocationController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/driver/location")
    public void updateDriverLocation(@RequestBody LocationMessage message) {
        try {
            log.info("Received location update via Kafka: {}", message);
            
            if (message.getDriverId() == null) {
                throw new IllegalArgumentException("ドライバーIDは必須です");
            }
            if (message.getLatitude() == 0.0 && message.getLongitude() == 0.0) {
                throw new IllegalArgumentException("有効な位置情報を指定してください");
            }
            
            kafkaProducerService.sendLocationUpdate(message);
        } catch (Exception e) {
            log.error("Error forwarding location update to Kafka: {}", e.getMessage(), e);
            throw e;
        }
    }
} 