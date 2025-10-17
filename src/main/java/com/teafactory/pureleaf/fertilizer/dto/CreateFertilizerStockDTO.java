package com.teafactory.pureleaf.fertilizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFertilizerStockDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "Weight per quantity is required")
    @Positive(message = "Weight per quantity must be positive")
    private Double weightPerQuantity;

    @NotNull(message = "Purchase price is required")
    @PositiveOrZero(message = "Purchase price must be zero or positive")
    private Double purchasePrice;

    @NotNull(message = "Sell price is required")
    @PositiveOrZero(message = "Sell price must be zero or positive")
    private Double sellPrice;

    @NotBlank(message = "Warehouse is required")
    private String warehouse;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;
}
