package com.teafactory.pureleaf.advanceProcess.dto;

import com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceStatusCountDto {
    private SupplierAdvance.Status status;
    private long count;
}

