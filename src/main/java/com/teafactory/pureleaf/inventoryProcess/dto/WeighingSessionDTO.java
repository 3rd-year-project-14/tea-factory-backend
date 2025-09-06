package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WeighingSessionDTO {
    private Long tripId;
    private Long sessionId;
    private LocalDate sessionDate;
    private Long userId;
    private String userName;
    private String status;
}

