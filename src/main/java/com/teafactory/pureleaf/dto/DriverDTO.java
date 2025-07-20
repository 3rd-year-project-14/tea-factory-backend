package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {
    private Long driverId;
    private String driverType;
    private String licenseImage;
    private String licenseStatus;
    private LocalDateTime licenseExpiry;
    private String vehicleNo;
    private Integer vehicleCapacity;
    private String emergencyContact;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long factoryId;
    private Long routeId;
    private Long userId;
}
