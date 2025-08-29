package com.teafactory.pureleaf.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequestStatusDTO {
    private Long id;
    private String status;
    private String rejectionReason;
    private LocalDate rejectedDate;
}
