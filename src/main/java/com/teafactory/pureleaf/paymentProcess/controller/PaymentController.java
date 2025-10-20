package com.teafactory.pureleaf.paymentProcess.controller;

import com.teafactory.pureleaf.paymentProcess.dto.*;
import com.teafactory.pureleaf.paymentProcess.entity.CashCollectionBatch;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentStatus;
import com.teafactory.pureleaf.paymentProcess.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // 1. POST /monthly/calculate
    @PostMapping("/monthly/calculate")
    public ResponseEntity<List<PaymentDTO>> calculateMonthly(@RequestBody @Valid Map<String, Object> body) {
        int month = (Integer) body.get("month");
        int year = (Integer) body.get("year");
        String factoryId = (String) body.get("factoryId");
        return ResponseEntity.ok(paymentService.calculateMonthlyPayments(month, year, factoryId));
    }

    // 2. GET /monthly/pending-approval
    @GetMapping("/monthly/pending-approval")
    public ResponseEntity<List<PaymentDTO>> getMonthlyPending(@RequestParam int month, @RequestParam int year, @RequestParam String factoryId) {
        return ResponseEntity.ok(paymentService.getMonthlyPaymentsForApproval(month, year, factoryId));
    }

    // 3. POST /monthly/approve
    @PostMapping("/monthly/approve")
    public ResponseEntity<List<PaymentDTO>> approveMonthly(@RequestBody @Valid PaymentApprovalRequestDTO request) {
        return ResponseEntity.ok(paymentService.approveMonthlyPayments(request));
    }

    // 4. POST /adhoc
    @PostMapping("/adhoc")
    public ResponseEntity<PaymentDTO> createAdhoc(@RequestBody @Valid PaymentDTO dto) {
        PaymentDTO created = paymentService.createAdhocPayment(dto);
        return ResponseEntity.status(201).body(created);
    }

    // 5. GET /adhoc/pending
    @GetMapping("/adhoc/pending")
    public ResponseEntity<List<PaymentDTO>> getPendingAdhoc(@RequestParam String factoryId, @RequestParam(required = false) String type) {
        PaymentTypeEnumWrapper wrapper = new PaymentTypeEnumWrapper();
        // parse optional type
        com.teafactory.pureleaf.paymentProcess.enums.PaymentType pt = null;
        if (type != null && !type.isBlank()) {
            pt = com.teafactory.pureleaf.paymentProcess.enums.PaymentType.valueOf(type);
        }
        return ResponseEntity.ok(paymentService.getPendingAdhocPayments(factoryId, pt));
    }

    // 6. POST /adhoc/{paymentId}/approve
    @PostMapping("/adhoc/{paymentId}/approve")
    public ResponseEntity<PaymentDTO> approveAdhoc(@PathVariable String paymentId, @RequestBody Map<String, String> body) {
        String approvedBy = body.get("approvedBy");
        return ResponseEntity.ok(paymentService.approveAdhocPayment(paymentId, approvedBy));
    }

    // 7. GET /bank/queue
    @GetMapping("/bank/queue")
    public ResponseEntity<List<PaymentDTO>> bankQueue(@RequestParam String factoryId) {
        return ResponseEntity.ok(paymentService.getBankPaymentsQueue(factoryId));
    }

    // 8. POST /bank/generate-csv
    @PostMapping("/bank/generate-csv")
    public ResponseEntity<BankCsvBatchDTO> generateCsv(@RequestBody @Valid BankCsvGenerationRequestDTO request) {
        BankCsvBatchDTO batch = paymentService.generateBankCsv(request);
        return ResponseEntity.status(201).body(batch);
    }

    // 9. GET /bank/csv/{batchId}/download
    @GetMapping("/bank/csv/{batchId}/download")
    public ResponseEntity<byte[]> downloadCsv(@PathVariable String batchId) {
        byte[] data = paymentService.downloadBankCsv(batchId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", batchId + ".csv");
        return ResponseEntity.ok().headers(headers).body(data);
    }

    // 10. GET /bank/csv/history
    @GetMapping("/bank/csv/history")
    public ResponseEntity<List<BankCsvBatchDTO>> csvHistory(@RequestParam(required = false) String factoryId,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(paymentService.getBankCsvHistory(factoryId, fromDate, toDate));
    }

    // 11. GET /cash/queue
    @GetMapping("/cash/queue")
    public ResponseEntity<Map<String, CashCollectionDTO>> cashQueue(@RequestParam String factoryId) {
        return ResponseEntity.ok(paymentService.getCashPaymentsQueue(factoryId));
    }

    // 12. GET /cash/route/{routeId}
    @GetMapping("/cash/route/{routeId}")
    public ResponseEntity<CashCollectionDTO> cashByRoute(@PathVariable String routeId) {
        return ResponseEntity.ok(paymentService.getCashPaymentsByRoute(routeId));
    }

    // 13. POST /cash/disburse
    @PostMapping("/cash/disburse")
    public ResponseEntity<CashCollectionBatch> disburseCash(@RequestBody @Valid CashDisbursementRequestDTO request) {
        CashCollectionBatch batch = paymentService.disburseCash(request);
        return ResponseEntity.status(201).body(batch);
    }

    // 14. GET /cash/history
    @GetMapping("/cash/history")
    public ResponseEntity<List<CashCollectionBatch>> cashHistory(@RequestParam String routeId,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.atTime(23,59,59);
        List<CashCollectionBatch> batches = paymentService.getCashCollectionHistory(routeId, from, to);
        return ResponseEntity.ok(batches);
    }

    // 15. GET /{paymentId}
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable String paymentId) {
        return paymentService.getPaymentById(paymentId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 16. GET /supplier/{supplierId}
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PaymentDTO>> paymentsBySupplier(@PathVariable String supplierId, PaymentFilterDTO filter) {
        return ResponseEntity.ok(paymentService.getPaymentsBySupplier(supplierId, filter));
    }

    // 17. GET /route/{routeId}
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<PaymentDTO>> paymentsByRoute(@PathVariable String routeId, PaymentFilterDTO filter) {
        // reuse getPaymentsBySupplier by building filter with route
        filter.setRouteId(routeId);
        Page<PaymentDTO> page = paymentService.getPaymentHistory(filter, Pageable.unpaged());
        return ResponseEntity.ok(page.getContent());
    }

    // 18. GET /history
    @GetMapping("/history")
    public ResponseEntity<Page<PaymentDTO>> paymentHistory(PaymentFilterDTO filter, Pageable pageable) {
        return ResponseEntity.ok(paymentService.getPaymentHistory(filter, pageable));
    }

    // 19. GET /summary
    @GetMapping("/summary")
    public ResponseEntity<List<PaymentSummaryDTO>> summary(@RequestParam int month, @RequestParam int year, @RequestParam String factoryId) {
        return ResponseEntity.ok(paymentService.getPaymentSummaryByRoute(month, year, factoryId));
    }

    // 20. GET /dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(@RequestParam int month, @RequestParam int year, @RequestParam String factoryId) {
        return ResponseEntity.ok(paymentService.getDashboardStatistics(month, year, factoryId));
    }

    // 21. PUT /{paymentId}/status
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDTO> updateStatus(@PathVariable String paymentId, @RequestBody Map<String, String> body) {
        PaymentStatus status = PaymentStatus.valueOf(body.get("status"));
        String updatedBy = body.get("updatedBy");
        return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentId, status, updatedBy));
    }

    // 22. POST /{paymentId}/cancel
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentDTO> cancelPayment(@PathVariable String paymentId, @RequestBody Map<String, String> body) {
        String cancelledBy = body.get("cancelledBy");
        String reason = body.get("reason");
        return ResponseEntity.ok(paymentService.cancelPayment(paymentId, cancelledBy, reason));
    }

    // 23. GET /supplier/{supplierId}/deductions
    @GetMapping("/supplier/{supplierId}/deductions")
    public ResponseEntity<List<DeductionDetailDTO>> supplierDeductions(@PathVariable String supplierId) {
        return ResponseEntity.ok(paymentService.getDeductionBreakdown(supplierId));
    }

    // 24. GET /supplier/{supplierId}/deductions/total
    @GetMapping("/supplier/{supplierId}/deductions/total")
    public ResponseEntity<Map<String, BigDecimal>> supplierDeductionsTotal(@PathVariable String supplierId) {
        BigDecimal total = paymentService.calculateDeductions(supplierId);
        return ResponseEntity.ok(Map.of("totalDeductions", total));
    }

    // For controller internal use: simple wrapper class placeholder
    private static class PaymentTypeEnumWrapper { }
}
