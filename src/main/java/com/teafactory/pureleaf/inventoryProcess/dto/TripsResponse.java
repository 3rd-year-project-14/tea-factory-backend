package com.teafactory.pureleaf.inventoryProcess.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripsResponse {
    private Long tripId;
    private String driverName;
    private Long routeId;
    private String routeName;
    private String tripDate;
    private String tripStatus;
    private int bagCount;
}
