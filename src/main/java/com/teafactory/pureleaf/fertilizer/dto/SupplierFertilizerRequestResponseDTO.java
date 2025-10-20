package com.teafactory.pureleaf.fertilizer.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SupplierFertilizerRequestResponseDTO {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private LocalDate requestDate;
    private String note;
    private String status;
    private List<SupplierFertilizerRequestItemResponseDTO> items;
}
