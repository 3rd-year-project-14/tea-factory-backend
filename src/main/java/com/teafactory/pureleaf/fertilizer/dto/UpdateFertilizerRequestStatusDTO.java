package com.teafactory.pureleaf.fertilizer.dto;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFertilizerRequestStatusDTO {
    @NotNull(message = "Status is required")
    private FertilizerRequestStatus status;

    private String rejectReason; // required if status == REJECTED
}
