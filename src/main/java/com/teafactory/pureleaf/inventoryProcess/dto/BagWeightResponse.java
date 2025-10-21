package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BagWeightResponse {
    private double grossWeight;
    private double netWeight;
    private double water;
    private double coarse;
}

