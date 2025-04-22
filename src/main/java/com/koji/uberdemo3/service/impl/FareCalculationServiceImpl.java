package com.koji.uberdemo3.service.impl;

import com.koji.uberdemo3.entity.Location;
import com.koji.uberdemo3.service.FareCalculationService;
import org.springframework.stereotype.Service;

@Service
public class FareCalculationServiceImpl implements FareCalculationService {
    private static final double BASE_FARE = 500.0; // 基本料金
    private static final double PER_KM_RATE = 100.0; // 1kmあたりの料金
    private static final double PER_MINUTE_RATE = 20.0; // 1分あたりの料金
    private static final double AVERAGE_SPEED_KMH = 30.0; // 平均速度（km/h）
    private static final double EARTH_RADIUS_KM = 6371.0; // 地球の半径（km）

    @Override
    public double calculateFare(Location pickupLocation, Location dropoffLocation, double surgeMultiplier) {
        if (surgeMultiplier < 1.0 || surgeMultiplier > 5.0) {
            throw new IllegalArgumentException("需要倍率は1.0から5.0の間でなければなりません");
        }

        double distance = calculateDistance(pickupLocation, dropoffLocation);
        double duration = calculateDuration(pickupLocation, dropoffLocation);

        // 料金計算: 基本料金 + (距離料金 + 時間料金) * 需要倍率
        double fare = BASE_FARE + (distance * PER_KM_RATE + duration * PER_MINUTE_RATE) * surgeMultiplier;
        
        // 最小料金を設定（基本料金 × 需要倍率）
        return Math.max(fare, BASE_FARE * surgeMultiplier);
    }

    @Override
    public double calculateDistance(Location location1, Location location2) {
        // ハバーサインの公式を使用して2地点間の距離を計算
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                  Math.cos(lat1) * Math.cos(lat2) *
                  Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public double calculateDuration(Location location1, Location location2) {
        double distance = calculateDistance(location1, location2);
        // 距離を平均速度で割って所要時間を計算（分単位）
        return (distance / AVERAGE_SPEED_KMH) * 60;
    }
} 