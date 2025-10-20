package com.teafactory.pureleaf.supplierMobileApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightSummaryDTO {
    private double totalNetWeight;
    private double totalGrossWeight;
    private double totalWet;
    private double totalCoarse;
    private double totalTareWeight;
}

