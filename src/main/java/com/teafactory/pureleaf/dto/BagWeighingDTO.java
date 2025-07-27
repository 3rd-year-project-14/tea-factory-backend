package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BagWeighingDTO {
    private Long id;
    private Long tripBagId;
    private Long sessionId;
    private Double grossWeight;
    private Double tareWeight;
    private Double netWeight;
    private Boolean wet;
    private Boolean coarse;
    private Double otherWeight;
    private String otherWeightReason;
    private String type;
    private LocalDateTime recordedAt;
}

