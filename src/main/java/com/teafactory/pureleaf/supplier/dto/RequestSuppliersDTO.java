package com.teafactory.pureleaf.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor@NoArgsConstructor
public class RequestSuppliersDTO {
    private Long supplierRequestId;
    private String name;
    private Double monthlySupply;
    private LocalDate requestDate;
    private LocalDate rejectedDate;
}
