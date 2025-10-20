package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CashDisbursementRequestDTO {
    @JsonProperty("routeId")
    @NotBlank
    private String routeId;
    @JsonProperty("driverId")
    @NotBlank
    private String driverId;
    @JsonProperty("paymentIds")
    @NotEmpty
    private List<@NotBlank String> paymentIds;
    @JsonProperty("collectedBy")
    @NotBlank
    private String collectedBy;
    @JsonProperty("totalAmount")
    @NotNull
    private BigDecimal totalAmount;
    @JsonProperty("notes")
    private String notes;
}

