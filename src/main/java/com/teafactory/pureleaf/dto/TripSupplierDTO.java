package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripSupplierDTO {
    private Long tripId;
    private Long supplierId;
    private LocalTime arrivedTime;
    private LocalTime completedTime;
    private String status;
}

