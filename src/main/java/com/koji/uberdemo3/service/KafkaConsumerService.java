package com.koji.uberdemo3.service;

import com.koji.uberdemo3.message.LocationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;
    private static final String TOPIC = "driver-locations";

    @KafkaListener(topics = TOPIC, groupId = "uber-group")
    public void consumeLocationUpdate(LocationMessage message) {
        log.info("Received location update from Kafka: {}", message);
        try {
            // WebSocketを通じてクライアントにメッセージを送信
            messagingTemplate.convertAndSend("/topic/driver-locations", message);
            log.info("Successfully sent message to WebSocket: {}", message);
        } catch (Exception e) {
            log.error("Error sending message to WebSocket: {}", e.getMessage(), e);
        }
    }
} 