package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.entity.DriverLocation;
import com.koji.uberdemo3.message.LocationMessage;
import com.koji.uberdemo3.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final KafkaProducerService kafkaProducerService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ws/driver/location")
    public void updateDriverLocation(DriverLocation driverLocation) {
        try {
            log.info("Received location update via WebSocket: {}", driverLocation);
            
            // Kafkaにメッセージを送信
            LocationMessage message = new LocationMessage();
            message.setDriverId(driverLocation.getDriverId());
            message.setLatitude(driverLocation.getLatitude());
            message.setLongitude(driverLocation.getLongitude());
            
            kafkaProducerService.sendLocationUpdate(message);
            
            // 更新された位置情報をブロードキャスト
            messagingTemplate.convertAndSend("/topic/driver-locations", driverLocation);
        } catch (Exception e) {
            log.error("Error updating driver location: {}", e.getMessage(), e);
            throw e;
        }
    }
} 