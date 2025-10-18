package com.teafactory.pureleaf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @Column(name = "vehicle_no", nullable = false, length = 100)
    private String vehicleNo;
    private String model;
    private Integer capacity;
    private String status;
    private LocalDateTime registeredDate;
    private String incomeCertificate;
    private String image;
    @ManyToOne
    @JoinColumn(name = "factory_id")
    private Factory factory;
}

