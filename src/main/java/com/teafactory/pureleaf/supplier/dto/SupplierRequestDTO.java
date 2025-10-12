package com.teafactory.pureleaf.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequestDTO {
    private Long id;
    private Long userId;
    private Long factoryId;
    private String status;
    private Double landSize;
    private Double monthlySupply;
    private String nicImage;
    private String rejectReason;
    private String pickupLocation;
    private String landLocation;
    private LocalDate rejectedDate;
}
