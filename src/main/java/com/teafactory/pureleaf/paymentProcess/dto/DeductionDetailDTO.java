package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentType;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeductionDetailDTO {
    @JsonProperty("paymentId")
    @NotBlank
    private String paymentId;

    @JsonProperty("deductionType")
    @NotNull
    private PaymentType deductionType;

    @JsonProperty("amount")
    @NotNull
    private BigDecimal amount;

    @JsonProperty("approvedDate")
    private LocalDateTime approvedDate;
}

