package com.teafactory.pureleaf.fertilizer.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFertilizerRequestDTO {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String note;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}
