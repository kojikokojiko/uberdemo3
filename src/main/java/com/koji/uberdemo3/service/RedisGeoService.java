package com.koji.uberdemo3.service;

import com.koji.uberdemo3.entity.DriverLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisGeoService {

    private static final String GEO_KEY = "driver:locations";
    private final RedisTemplate<String, String> redisTemplate;

    public void addDriverLocation(Long driverId, double latitude, double longitude) {
        log.info("Adding driver location to Redis GEO - driverId: {}, lat: {}, lon: {}", driverId, latitude, longitude);
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), String.valueOf(driverId));
    }

    public List<DriverLocation> findNearbyDrivers(double latitude, double longitude, double radiusInKm) {
        log.info("Finding nearby drivers - lat: {}, lon: {}, radius: {}km", latitude, longitude, radiusInKm);
        
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, Metrics.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortAscending();

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo()
                .radius(GEO_KEY, circle, args);

        return results.getContent().stream()
                .map(geoResult -> {
                    DriverLocation driverLocation = new DriverLocation();
                    driverLocation.setDriverId(Long.parseLong(geoResult.getContent().getName()));
                    driverLocation.setLatitude(geoResult.getContent().getPoint().getY());
                    driverLocation.setLongitude(geoResult.getContent().getPoint().getX());
                    driverLocation.setDistance(geoResult.getDistance().getValue());
                    return driverLocation;
                })
                .collect(Collectors.toList());
    }

    public void removeDriverLocation(Long driverId) {
        log.info("Removing driver location from Redis GEO - driverId: {}", driverId);
        redisTemplate.opsForGeo().remove(GEO_KEY, String.valueOf(driverId));
    }
} 