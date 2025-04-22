package com.koji.uberdemo3.controller;

import com.koji.uberdemo3.entity.DriverLocation;
import com.koji.uberdemo3.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class WebSocketController {

    private final LocationService locationService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(LocationService locationService, SimpMessagingTemplate messagingTemplate) {
        this.locationService = locationService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/driver/location")
    public void updateDriverLocation(@RequestBody DriverLocation driverLocation) {
        try {
            log.info("Received location update: {}", driverLocation);
            
            // 位置情報を更新
            DriverLocation updatedLocation = locationService.updateDriverLocation(
                driverLocation.getDriver(),
                driverLocation.getLocation(),
                driverLocation.getIsAvailable()
            );
            
            log.info("Updated location: {}", updatedLocation);
            
            // 更新された位置情報をブロードキャスト
            messagingTemplate.convertAndSend("/topic/driver-locations", updatedLocation);
        } catch (Exception e) {
            log.error("Error updating driver location: {}", e.getMessage(), e);
            throw e;
        }
    }
} 