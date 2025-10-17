package com.teafactory.pureleaf.Transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private Long vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private Integer capacity;
    private String status;
    private Long assignedDriverId;
    private String assignedDriverName;
    private LocalDate lastServiceDate;
    private String vehicleImage;
    private LocalDateTime createdAt;
}

