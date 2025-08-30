package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeaSupplyRequestDTO {
    private Long requestId;
    private Long supplierId;
    private LocalDate supplyDate;
    private Integer estimatedBagCount;
    private String status;
}

