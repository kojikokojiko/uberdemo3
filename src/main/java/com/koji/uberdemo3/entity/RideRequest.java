package com.koji.uberdemo3.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ride_requests")
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "pickup_location_id", nullable = false)
    private Location pickupLocation;

    @ManyToOne
    @JoinColumn(name = "dropoff_location_id", nullable = false)
    private Location dropoffLocation;

    @Column(nullable = false)
    private LocalDateTime requestTime;

    @Column
    private LocalDateTime acceptedTime;

    @Column
    private LocalDateTime completedTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status;

    @Column(nullable = false)
    private Double estimatedFare;

    public enum RideStatus {
        PENDING,    // リクエスト送信後、ドライバー待ち
        ACCEPTED,   // ドライバーが承諾
        IN_PROGRESS,// 乗車中
        COMPLETED,  // 完了
        CANCELLED   // キャンセル
    }
} 