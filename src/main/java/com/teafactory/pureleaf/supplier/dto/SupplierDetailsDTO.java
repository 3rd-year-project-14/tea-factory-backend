package com.teafactory.pureleaf.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDetailsDTO {
    private Long supplierId;
    private String supplierName;
    private String routeName;
    private LocalDate approvedDate;
}

