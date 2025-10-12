package com.teafactory.pureleaf.inventoryProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripStatusCountsDTO {
    private Long factoryId;
    private LocalDate date;
    private long pendingCount;    // pending + collected
    private long arrivedCount;    // arrived
    private long weighedCount;    // weighed
    private long completedCount;  // completed
}

