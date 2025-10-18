package com.teafactory.pureleaf.driverProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRegistrationDTO {
    // User fields
    private String firebaseUid;
    private String email;
    private String name;
    private String nic;
    private String contactNo;
    private String address;
    private Long factoryId;

    // Driver fields
    private String driverType;
    private String licenseImage;
    private String licenseStatus;
    private LocalDate licenseExpiry;
    private String vehicleNo;
    private Integer vehicleCapacity;
    private String emergencyContact;
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}
