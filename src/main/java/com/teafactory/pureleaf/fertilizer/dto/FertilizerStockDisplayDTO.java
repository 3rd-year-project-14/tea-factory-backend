package com.teafactory.pureleaf.fertilizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FertilizerStockDisplayDTO {
    private Long fertilizerStockId;
    private String productName;
    private Double weightPerQuantity;
    private Double sellPrice;
}

