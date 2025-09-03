package com.teafactory.pureleaf.driverProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {
    private Long driverId;
    private String driverType;
    private String licenseImage;
    private String licenseStatus;
    private LocalDate licenseExpiry;
    private String vehicleNo;
    private Integer vehicleCapacity;
    private String emergencyContact;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long factoryId;
    private Long routeId;
    private Long userId;
    private String routeName;
    private String routeStartLocation;
    private String routeEndLocation;
    private String factoryName;
    private String factoryMapUrl;
}
