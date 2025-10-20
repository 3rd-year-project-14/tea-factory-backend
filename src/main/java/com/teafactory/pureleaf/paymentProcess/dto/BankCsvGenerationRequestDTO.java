package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankCsvGenerationRequestDTO {
    @JsonProperty("paymentIds")
    @NotEmpty
    private List<@NotBlank String> paymentIds;
    @JsonProperty("generatedBy")
    @NotBlank
    private String generatedBy;
    @JsonProperty("factoryId")
    @NotBlank
    private String factoryId;
}

