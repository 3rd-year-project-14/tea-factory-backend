package com.teafactory.pureleaf.driverProcess.entity;

import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    private String driverType; // inhouse or privet
    private String licenseImage;
    private String licenseStatus;
    private LocalDate licenseExpiry;
    private String vehicleNo;
    private Integer vehicleCapacity;
    private String emergencyContact;
    private Boolean isActive;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "factory_id")
    private Factory factory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
