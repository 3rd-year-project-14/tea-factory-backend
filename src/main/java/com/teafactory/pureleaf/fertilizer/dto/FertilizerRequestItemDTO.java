package com.teafactory.pureleaf.fertilizer.dto;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerRequestItemStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertilizerRequestItemDTO {
    private Long id;
    private Long fertilizerStockId;
    private Integer quantity;
    private String productName;
    private Double weightPerQuantity;
    private Long supplierId;
    private String supplierName;
    private FertilizerRequestItemStatus status;
    private String rejectReason;
}
