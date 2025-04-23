package com.koji.uberdemo3.service;

import com.koji.uberdemo3.message.LocationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "driver-locations";
    private final KafkaTemplate<String, LocationMessage> kafkaTemplate;

    public void sendLocationUpdate(LocationMessage message) {
        log.info("Sending location update to Kafka: {}", message);
        kafkaTemplate.send(TOPIC, String.valueOf(message.getDriverId()), message)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Location update sent successfully");
                } else {
                    log.error("Failed to send location update", ex);
                }
            });
    }
} 