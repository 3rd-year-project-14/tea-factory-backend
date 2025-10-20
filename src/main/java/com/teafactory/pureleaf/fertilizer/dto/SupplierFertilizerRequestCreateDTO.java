package com.teafactory.pureleaf.fertilizer.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SupplierFertilizerRequestCreateDTO {
    private Long supplierId;
    private LocalDate requestDate;
    private String note;
    private List<SupplierFertilizerRequestItemCreateDTO> items;
}
