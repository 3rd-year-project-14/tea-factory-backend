package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teafactory.pureleaf.paymentProcess.enums.DisbursementMethod;
import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonthlyPaymentCalculationDTO {
    @JsonProperty("supplierId")
    @NotBlank
    private String supplierId;

    @JsonProperty("supplierName")
    private String supplierName;

    @JsonProperty("routeId")
    @NotBlank
    private String routeId;

    @JsonProperty("routeName")
    private String routeName;

    @JsonProperty("totalWeight")
    @NotNull
    private BigDecimal totalWeight;

    @JsonProperty("teaRate")
    @NotNull
    private BigDecimal teaRate;

    @JsonProperty("grossAmount")
    @NotNull
    private BigDecimal grossAmount;

    @JsonProperty("deductionAmount")
    @NotNull
    private BigDecimal deductionAmount;

    @JsonProperty("deductionBreakdown")
    @Valid
    private List<DeductionDetailDTO> deductions;

    @JsonProperty("netAmount")
    @NotNull
    private BigDecimal netAmount;

    @JsonProperty("disbursementMethod")
    @NotNull
    private DisbursementMethod disbursementMethod;
}

