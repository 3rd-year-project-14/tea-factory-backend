package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripSummaryResponseDTO {
    private Long tripId;
    private Long routeId;
    private String routeName;
    private String driverName;
    private long bagCount;
    private Double grossWeight;
    private Double totalTareWeight;
}
