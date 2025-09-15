package com.teafactory.pureleaf.fertilizer.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {
    private Long id;
    private Long categoryId;
    private String categoryName; // for display only
    private Long companyId;
    private String companyName; // for display only
    private Double weight;
    private Integer quantity;
    private String warehouseName;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
}