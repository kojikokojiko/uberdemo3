package com.koji.uberdemo3.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String email;
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    private UserType userType; // DRIVER or RIDER
    
    private boolean isActive;
}
