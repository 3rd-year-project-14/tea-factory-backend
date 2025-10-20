package com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SupplierMonthlySummaryDTO {
    private Long supplierId;
    private String supplierName;
    private String contactNumber;
    private LocalDate lastDelivery;
    private BigDecimal totalNetWeight;
    private Long totalBags;
    private Integer totalDeliveryDays;
    private BigDecimal totalGrossWeight;
}
