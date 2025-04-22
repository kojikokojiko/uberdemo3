package com.koji.uberdemo3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "緯度は必須です")
    @Min(value = -90, message = "緯度は-90から90の間でなければなりません")
    @Max(value = 90, message = "緯度は-90から90の間でなければなりません")
    private Double latitude;

    @NotNull(message = "経度は必須です")
    @Min(value = -180, message = "経度は-180から180の間でなければなりません")
    @Max(value = 180, message = "経度は-180から180の間でなければなりません")
    private Double longitude;

    @NotBlank(message = "住所は必須です")
    private String address;

    @NotBlank(message = "都市名は必須です")
    private String city;

    @NotBlank(message = "州/県名は必須です")
    private String state;

    @NotBlank(message = "国名は必須です")
    private String country;

    @NotBlank(message = "郵便番号は必須です")
    private String postalCode;
} 