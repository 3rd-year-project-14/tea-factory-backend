package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDTO {
    private Long tripId;
    private Long driverId;
    private LocalDate tripDate;
    private LocalTime startTime;
    private LocalTime endTime;
}

