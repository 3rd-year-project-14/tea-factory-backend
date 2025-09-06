package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BagWeightDTO {
    private Long id;
    private double coarse;
    private double water;
    private LocalDate date;
    private double grossWeight;
    private double netWeight;
    private LocalDateTime recordedAt;
    private double tareWeight;
    private Long supplyRequestId;
    private Long sessionId;
    private double otherWeight;
    private String reason;
    private List<String> bagNumbers;
}
