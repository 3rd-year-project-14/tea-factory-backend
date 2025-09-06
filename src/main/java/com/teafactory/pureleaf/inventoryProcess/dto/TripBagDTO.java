package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripBagDTO {
    private Long id;
    private Long tripId;
    private Long supplyRequestId;
    private Long routeId;
    private String bagNumber;
    private Double driverWeight;
    private Boolean wet;
    private Boolean coarse;
    private String type;
    private String note;
}

