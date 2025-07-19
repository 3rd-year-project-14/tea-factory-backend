package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequestDTO {
    private Long userId;
    private String status;
    private Double landSize;
    private Double monthlySupply;
    private String requestedRoute;
    private String nicImage;
    private String rejectReason;
    private String pickupLocation;
    private String landLocation;
    private LocalDateTime rejectedDate;
}
