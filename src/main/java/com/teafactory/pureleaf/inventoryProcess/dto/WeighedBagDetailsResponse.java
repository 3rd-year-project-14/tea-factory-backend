package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeighedBagDetailsResponse {
    private String bagNumber;
    private Long supplyRequestId;
}

