package com.teafactory.pureleaf.dto;

import lombok.Data;

@Data
public class BagWeightWithSupplierDTO {
    private Long bagWeightId;
    private double coarse;
    private double water;
    private double grossWeight;
    private double netWeight;
    private double tareWeight;
    private double otherWeight;
    private String reason;
    private int bagTotal;
    private Long supplierId;
    private String supplierName;
}

