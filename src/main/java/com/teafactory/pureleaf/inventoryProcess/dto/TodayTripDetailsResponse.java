package com.teafactory.pureleaf.inventoryProcess.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodayTripDetailsResponse {
    private Long tripId;
    private String routeName;
    private String routeCode;
    private String driverName;
    private Integer totalBags;
    private Double totalWeight;
    private Integer totalSuppliers;
    private Integer completedSuppliers;
    private String tripStatus;
    private LocalTime lastUpdate;
}
