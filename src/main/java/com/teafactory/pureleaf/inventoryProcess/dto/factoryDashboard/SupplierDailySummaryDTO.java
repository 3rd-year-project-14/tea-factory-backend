package com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SupplierDailySummaryDTO {
    private Integer day;
    private Long bagCount;
    private BigDecimal grossWeight;
    private BigDecimal bagWeight;
    private BigDecimal water;
    private BigDecimal coarseLeaf;
    private BigDecimal netWeight;
}

