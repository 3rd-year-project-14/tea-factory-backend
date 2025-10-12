package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripBagDetailsDTO {
    private String bagNo;
    private Double driverWeight;
    private Boolean wet;
    private Boolean coarse;
}


