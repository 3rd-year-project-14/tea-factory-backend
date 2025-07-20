package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverAvailabilityDTO {
    private Long id;
    private Long driverId;
    private LocalDate date;
    private Boolean isAvailable;
    private String reason;
}

