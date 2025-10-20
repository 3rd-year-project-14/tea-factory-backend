package com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRouteSummaryDTO {
    private Long supplierId;
    private String supplierName;
    private double totalWeight;
    private long totalBags;
    private double totalNetWeight;
}

