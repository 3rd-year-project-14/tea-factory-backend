package com.teafactory.pureleaf.inventoryProcess.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateBagsRequest {
    @NotNull(message = "routeId is required")
    private Long routeId;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;
}

