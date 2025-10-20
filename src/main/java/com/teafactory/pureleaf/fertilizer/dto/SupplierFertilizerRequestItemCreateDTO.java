package com.teafactory.pureleaf.fertilizer.dto;

import lombok.Data;

@Data
public class SupplierFertilizerRequestItemCreateDTO {
    private Long fertilizerStockId;
    private Integer quantity;
}

