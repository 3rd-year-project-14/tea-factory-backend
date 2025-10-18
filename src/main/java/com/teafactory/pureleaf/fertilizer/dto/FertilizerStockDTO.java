package com.teafactory.pureleaf.fertilizer.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertilizerStockDTO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Long companyId;
    private Double weightPerQuantity;
    private Double purchasePrice;
    private Double sellPrice;
    private String warehouse;
    private Integer quantity;
    private Date createdAt;
    private String companyName;
    private String categoryName;
}
