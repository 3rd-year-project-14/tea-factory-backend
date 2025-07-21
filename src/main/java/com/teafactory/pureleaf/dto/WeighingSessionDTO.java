package com.teafactory.pureleaf.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WeighingSessionDTO {
    private Long tripId;
    private LocalDate sessionDate;
    private Long userId;
    private String userName;
    private String status;
}

