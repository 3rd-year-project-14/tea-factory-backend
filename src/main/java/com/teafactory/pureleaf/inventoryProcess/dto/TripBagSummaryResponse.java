package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripBagSummaryResponse {
    private Long tripId;
    private Long sessionId;
    private Long userId;
    private long supplierRequestCount;
    private long totalBags;
    private Double totalWeight;
}

