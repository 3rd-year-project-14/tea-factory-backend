package com.teafactory.pureleaf.Transport.entity;

import com.teafactory.pureleaf.driverProcess.entity.Driver;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "TransportVehicle")
@Table(name = "transport_vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    private String vehicleType;

    private Integer capacity;

    private String status; // e.g., AVAILABLE, ON_TRIP, MAINTENANCE

    @ManyToOne
    @JoinColumn(name = "assigned_driver_id")
    private Driver assignedDriver;

    private LocalDate lastServiceDate;

    private String vehicleImage; // store image URL or path

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
