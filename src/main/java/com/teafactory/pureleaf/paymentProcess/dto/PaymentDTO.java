package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teafactory.pureleaf.paymentProcess.enums.DisbursementMethod;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentStatus;
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
public class PaymentDTO {
    @JsonProperty("id")
    @NotBlank
    private String id;

    @JsonProperty("paymentType")
    @NotNull
    private PaymentType paymentType;

    @JsonProperty("supplierId")
    @NotBlank
    private String supplierId;

    @JsonProperty("routeId")
    @NotBlank
    private String routeId;

    @JsonProperty("periodMonth")
    @Min(1)
    @Max(12)
    private Integer periodMonth;

    @JsonProperty("periodYear")
    @Min(2000)
    @Max(2100)
    private Integer periodYear;

    @JsonProperty("grossAmount")
    @NotNull
    private BigDecimal grossAmount;

    @JsonProperty("totalWeight")
    private BigDecimal totalWeight;

    @JsonProperty("teaRate")
    private BigDecimal teaRate;

    @JsonProperty("deductionAmount")
    private BigDecimal deductionAmount;

    @JsonProperty("netAmount")
    @NotNull
    private BigDecimal netAmount;

    @JsonProperty("disbursementMethod")
    @NotNull
    private DisbursementMethod disbursementMethod;

    @JsonProperty("status")
    @NotNull
    private PaymentStatus status;

    @JsonProperty("linkedMonthlyPaymentId")
    private String linkedMonthlyPaymentId;

    @JsonProperty("isDeduction")
    private Boolean isDeduction;

    @JsonProperty("batchId")
    private String batchId;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("approvedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    @JsonProperty("approvedBy")
    private String approvedBy;

    @JsonProperty("disbursedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime disbursedAt;

    @JsonProperty("disbursedBy")
    private String disbursedBy;

    @JsonProperty("updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Calculated fields
    @JsonProperty("supplierName")
    private String supplierName;

    @JsonProperty("routeName")
    private String routeName;
}

