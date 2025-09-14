package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripBagBriefDTO {
    private String bagNo;
    private Double weight;
    private Boolean wet;
    private Boolean coarse;
    private Long supplyRequestId;
}
