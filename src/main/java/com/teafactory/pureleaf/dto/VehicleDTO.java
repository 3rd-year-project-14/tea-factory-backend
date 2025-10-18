package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private String vehicleNo;
    private String model;
    private Integer capacity;
    private String incomeCertificate;
    private String image;
    private Long factoryId; // added so client can pass factory id
}
