package com.teafactory.pureleaf.fertilizer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchFertilizerRequestDTO {
    @NotEmpty(message = "Requests list cannot be empty")
    @Valid
    private List<CreateFertilizerRequestDTO> requests;
    private LocalDate requestDate;
}
