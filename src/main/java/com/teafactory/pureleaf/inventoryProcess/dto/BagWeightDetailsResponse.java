package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.Data;

@Data
public class BagWeightDetailsResponse {
    private Long bagWeightId;
    private double grossWeight;
    private double deduction;
    private double netWeight;
    private Long supplierId;
    private String supplierName;
}
