package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BagWeightResponseDTO {
    private Long id;
    private double coarse;
    private double water;
    private LocalDate date;
    private double grossWeight;
    private double netWeight;
    private LocalDateTime recordedAt;
    private double tareWeight;
    private double otherWeight;
    private String reason;
    private int bagTotal;
}
