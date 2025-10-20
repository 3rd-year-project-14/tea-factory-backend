package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CashCollectionDTO {
    @JsonProperty("routeId")
    private String routeId;
    @JsonProperty("routeName")
    private String routeName;
    @JsonProperty("driverId")
    private String driverId;
    @JsonProperty("driverName")
    private String driverName;
    @JsonProperty("monthlyPayments")
    @Valid
    private List<PaymentDTO> monthlyPayments;
    @JsonProperty("adhocPayments")
    @Valid
    private List<PaymentDTO> adhocPayments;
    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;
}

