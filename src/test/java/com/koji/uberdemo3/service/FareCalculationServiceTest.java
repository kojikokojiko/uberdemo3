package com.koji.uberdemo3.service;

import com.koji.uberdemo3.entity.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FareCalculationServiceTest {

    @Autowired
    private FareCalculationService fareCalculationService;

    private Location tokyoStation;
    private Location shibuyaStation;

    @BeforeEach
    void setUp() {
        tokyoStation = new Location();
        tokyoStation.setLatitude(35.6812);
        tokyoStation.setLongitude(139.7671);

        shibuyaStation = new Location();
        shibuyaStation.setLatitude(35.6580);
        shibuyaStation.setLongitude(139.7016);
    }

    @Test
    void calculateFare_ShouldReturnCorrectFare() {
        // Given
        double surgeMultiplier = 1.0;

        // When
        double fare = fareCalculationService.calculateFare(tokyoStation, shibuyaStation, surgeMultiplier);

        // Then
        assertTrue(fare >= 500.0); // 基本料金以上
        assertTrue(fare <= 10000.0); // 妥当な上限値
    }

    @Test
    void calculateFare_ShouldApplySurgeMultiplier() {
        // Given
        double normalSurge = 1.0;
        double highSurge = 2.0;

        // When
        double normalFare = fareCalculationService.calculateFare(tokyoStation, shibuyaStation, normalSurge);
        double highFare = fareCalculationService.calculateFare(tokyoStation, shibuyaStation, highSurge);

        // Then
        assertTrue(highFare > normalFare);
        assertEquals(highFare / normalFare, highSurge, 0.1); // 許容誤差0.1
    }

    @Test
    void calculateDistance_ShouldReturnCorrectDistance() {
        // When
        double distance = fareCalculationService.calculateDistance(tokyoStation, shibuyaStation);

        // Then
        assertTrue(distance > 0);
        assertTrue(distance < 50); // 東京駅から渋谷駅までの距離は約6km
    }

    @Test
    void calculateDuration_ShouldReturnCorrectDuration() {
        // When
        double duration = fareCalculationService.calculateDuration(tokyoStation, shibuyaStation);

        // Then
        assertTrue(duration > 0);
        assertTrue(duration < 60); // 東京駅から渋谷駅までの所要時間は約15分
    }

    @Test
    void calculateFare_ShouldReturnMinimumFare() {
        // Given
        Location sameLocation = new Location();
        sameLocation.setLatitude(35.6812);
        sameLocation.setLongitude(139.7671);
        double surgeMultiplier = 1.0;

        // When
        double fare = fareCalculationService.calculateFare(sameLocation, sameLocation, surgeMultiplier);

        // Then
        assertEquals(500.0, fare); // 最小料金（基本料金）と一致
    }
} 