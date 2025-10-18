package com.teafactory.pureleaf.fertilizer.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFertilizerRequestDTO {
    @NotNull(message = "Fertilizer Stock ID is required")
    private Long fertilizerStockId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
