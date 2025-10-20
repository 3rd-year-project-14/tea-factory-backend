package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teafactory.pureleaf.paymentProcess.enums.DisbursementMethod;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentStatus;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentType;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFilterDTO {
    @JsonProperty("periodMonth")
    private Integer periodMonth;
    @JsonProperty("periodYear")
    private Integer periodYear;
    @JsonProperty("routeId")
    private String routeId;
    @JsonProperty("supplierId")
    private String supplierId;
    @JsonProperty("paymentType")
    private PaymentType paymentType;
    @JsonProperty("status")
    private PaymentStatus status;
    @JsonProperty("disbursementMethod")
    private DisbursementMethod disbursementMethod;
    @JsonProperty("fromDate")
    private LocalDate fromDate;
    @JsonProperty("toDate")
    private LocalDate toDate;
}

