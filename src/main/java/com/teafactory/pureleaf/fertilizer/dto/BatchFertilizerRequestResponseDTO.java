package com.teafactory.pureleaf.fertilizer.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchFertilizerRequestResponseDTO {
    private Long requestId;
    private List<FertilizerRequestItemDTO> items;
}

