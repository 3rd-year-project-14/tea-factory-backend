package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentSummaryDTO {
    @JsonProperty("routeId")
    @NotBlank
    private String routeId;

    @JsonProperty("routeName")
    private String routeName;

    @JsonProperty("totalPayments")
    @NotNull
    private Integer totalPayments;

    @JsonProperty("totalAmount")
    @NotNull
    private BigDecimal totalAmount;

    @JsonProperty("bankAmount")
    private BigDecimal bankAmount;

    @JsonProperty("cashAmount")
    private BigDecimal cashAmount;

    @JsonProperty("supplierCount")
    private Integer supplierCount;
}

