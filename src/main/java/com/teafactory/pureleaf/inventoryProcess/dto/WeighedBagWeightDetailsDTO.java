package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeighedBagWeightDetailsDTO {
    private Long id;
    private Integer bagTotal;
    private Integer coarse;
    private Integer water;
    private Integer otherWeight;
    private Double grossWeight;
    private Double tareWeight;
    private Long supplierId;
    private String supplierName;
    private String status;
}









