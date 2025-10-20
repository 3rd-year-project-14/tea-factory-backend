package com.teafactory.pureleaf.paymentProcess.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankCsvBatchDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("batchNumber")
    private Long batchNumber;

    @JsonProperty("factoryId")
    private String factoryId;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("filePath")
    private String filePath;

    @JsonProperty("totalPayments")
    private Integer totalPayments;

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("status")
    private String status;

    @JsonProperty("generatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generatedAt;

    @JsonProperty("generatedBy")
    private String generatedBy;

    @JsonProperty("sentToBankAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentToBankAt;

    @JsonProperty("confirmedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmedAt;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("payments")
    @Valid
    private List<PaymentDTO> payments;

    @JsonProperty("totalCount")
    private Integer totalCount;
}
