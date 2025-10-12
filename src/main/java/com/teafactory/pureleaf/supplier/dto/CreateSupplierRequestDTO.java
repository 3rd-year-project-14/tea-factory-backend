package com.teafactory.pureleaf.supplier.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSupplierRequestDTO {
    @NotNull
    private Long userId;

    @NotNull
    private String pickupLocation;

    @NotNull
    private Long factoryId;

    private Double landSize;

    private Double monthlySupply;

    private String nicImage;

    private String landLocation;
}
