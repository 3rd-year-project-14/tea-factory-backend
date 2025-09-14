package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeighingSummaryResponse {
    private long totalSuppliers;
    private long totalBags;
    private double totalGrossWeight;
}
