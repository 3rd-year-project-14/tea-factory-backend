package com.teafactory.pureleaf.fertilizer.dto;

import lombok.Data;

@Data
public class SupplierFertilizerRequestItemResponseDTO {
    private Long id;
    private Long fertilizerStockId;
    private String fertilizerStockName;
    private Integer quantity;
    private String status;
    private String rejectReason;
}

