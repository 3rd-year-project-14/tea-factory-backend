package com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventorySummaryDto {
    private double totalGrossWeight;
    private double totalNetWeight;
    private long totalBags;
}
