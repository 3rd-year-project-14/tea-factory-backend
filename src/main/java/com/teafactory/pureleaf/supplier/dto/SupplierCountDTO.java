package com.teafactory.pureleaf.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierCountDTO {
    private Long activeSupplierCount;
    private Long pendingRequestCount;
    private Long rejectedRequestCount;
}
