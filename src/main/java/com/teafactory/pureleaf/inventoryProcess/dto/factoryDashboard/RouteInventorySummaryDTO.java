package com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteInventorySummaryDTO {
    private Long routeId;
    private String routeName;
    private String routeCode;
    private long supplierCount;
    private double totalGrossWeight;
    private double netWeight;
}
