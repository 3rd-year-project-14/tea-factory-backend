package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripBagDetailsResponse {
    private String bagNumber;
    private Long supplierId;
    private String supplierName;
    private Double driverWeight;
    private Long supplyRequestId;
    private Boolean wet;
    private Boolean coarse;
    private Long bagId;
    private String status;
}
