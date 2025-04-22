package com.koji.uberdemo3.service;

import com.koji.uberdemo3.entity.Location;

public interface FareCalculationService {
    /**
     * 乗車リクエストの料金を計算します
     * @param pickupLocation 乗車地点
     * @param dropoffLocation 降車地点
     * @param surgeMultiplier 需要に基づく料金倍率
     * @return 計算された料金
     */
    double calculateFare(Location pickupLocation, Location dropoffLocation, double surgeMultiplier);

    /**
     * 2地点間の距離を計算します
     * @param location1 地点1
     * @param location2 地点2
     * @return 距離（キロメートル）
     */
    double calculateDistance(Location location1, Location location2);

    /**
     * 2地点間の所要時間を計算します
     * @param location1 地点1
     * @param location2 地点2
     * @return 所要時間（分）
     */
    double calculateDuration(Location location1, Location location2);
} 