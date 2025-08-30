package com.teafactory.pureleaf.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSupplierRequestDTO {
    private Long id;
    private Long userId;
    private Double landSize;
    private Double monthlySupply;
    private String nicImage;
    private String pickupLocation;
    private String landLocation;
    private Long factoryId;
}
