package com.teafactory.pureleaf.paymentProcess.service;

import com.teafactory.pureleaf.paymentProcess.dto.*;
import com.teafactory.pureleaf.paymentProcess.entity.*;
import com.teafactory.pureleaf.paymentProcess.enums.*;
import com.teafactory.pureleaf.paymentProcess.exception.*;
import com.teafactory.pureleaf.paymentProcess.repository.*;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.paymentProcess.dto.BankCsvBatchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import com.teafactory.pureleaf.payment.repository.TeaRateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentDeductionRepository paymentDeductionRepository;
    private final BankCsvBatchRepository bankCsvBatchRepository;
    private final BankCsvPaymentRepository bankCsvPaymentRepository;
    private final CashCollectionBatchRepository cashCollectionBatchRepository;
    private final CashCollectionPaymentRepository cashCollectionPaymentRepository;
    private final PaymentAuditLogRepository paymentAuditLogRepository;
    private final SupplierRepository supplierRepository;
    private final RouteRepository routeRepository;
    private final BagWeightRepository bagWeightRepository;
    private final TeaRateRepository teaRateRepository;
    private final ModelMapper modelMapper;

    // Helper: resolve Route from payment.routeId (stored as String). Route IDs are Long in the routes module.
    private Route resolveRouteByStringId(String routeIdStr) {
        if (routeIdStr == null) return null;
        try {
            Long id = Long.parseLong(routeIdStr);
            return routeRepository.findById(id).orElse(null);
        } catch (NumberFormatException ex) {
            // routeIdStr may not be a numeric id; try to match by routeCode if repository supports it (not available here)
            return null;
        }
    }

    // ...method implementations will be added here...

    @Transactional
    public List<PaymentDTO> calculateMonthlyPayments(int month, int year, String factoryId) {
        log.info("Calculating monthly payments for month={}, year={}, factoryId={}", month, year, factoryId);
        // 1. Get all active suppliers for the factory
        Long factoryIdLong;
        try {
            factoryIdLong = Long.parseLong(factoryId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid factoryId: " + factoryId);
        }
        List<Supplier> suppliers = supplierRepository.findByFactory_FactoryIdAndIsActiveTrue(factoryIdLong);
        if (suppliers == null || suppliers.isEmpty()) {
            log.warn("No active suppliers found for factory {}", factoryId);
            throw new IllegalArgumentException("No active suppliers found for factory: " + factoryId);
        }
        LocalDate periodDate = LocalDate.of(year, month, 1);
        String monthString = periodDate.getYear() + "-" + String.format("%02d", periodDate.getMonthValue());
        // Get current tea rate from teaRate table (global for all factories)
        BigDecimal teaRate = teaRateRepository.findCurrentRateByDate(monthString);
        if (teaRate == null) {
            log.error("No tea rate found for period {}", monthString);
            throw new InsufficientPaymentDataException("No tea rate found for period " + monthString);
        }
        List<Payment> payments = new java.util.ArrayList<>();
        for (Supplier supplier : suppliers) {
            try {
                // a) Get total weight from bagWeight table for the period
                BigDecimal totalWeight = bagWeightRepository.sumWeightBySupplierAndPeriod(supplier.getSupplierId(), month, year);
                if (totalWeight == null) totalWeight = BigDecimal.ZERO;
                // b) Use the global tea rate
                // c) Calculate gross amount
                BigDecimal grossAmount = totalWeight.multiply(teaRate);
                // d) Get pending deductions
                List<Payment> deductions = paymentRepository.findPendingDeductionsForSupplier(String.valueOf(supplier.getSupplierId()));
                BigDecimal totalDeductions = deductions.stream().map(Payment::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                // e) Calculate net amount
                BigDecimal netAmount = grossAmount.subtract(totalDeductions);
                // f) Create Payment entity
                String paymentId = generatePaymentId(PaymentType.MONTHLY, periodDate);
                String pref = supplier.getPaymentMethodPreference() != null ? supplier.getPaymentMethodPreference() : "CASH";
                DisbursementMethod disbursementMethod = switch (pref.toUpperCase()) {
                    case "BANK" -> DisbursementMethod.BANK;
                    case "CASH" -> DisbursementMethod.CASH;
                    default -> DisbursementMethod.CASH;
                };
                Payment payment = Payment.builder()
                        .id(paymentId)
                        .paymentType(PaymentType.MONTHLY)
                        .supplierId(String.valueOf(supplier.getSupplierId()))
                        .routeId(String.valueOf(supplier.getRoute().getRouteId()))
                        .periodMonth(month)
                        .periodYear(year)
                        .grossAmount(grossAmount)
                        .totalWeight(totalWeight)
                        .teaRate(teaRate)
                        .deductionAmount(totalDeductions)
                        .netAmount(netAmount)
                        .disbursementMethod(disbursementMethod)
                        .status(PaymentStatus.CALCULATED)
                        .isDeduction(false)
                        .build();
                paymentRepository.save(payment);
                // g) Link deductions
                for (Payment deduction : deductions) {
                    PaymentDeduction pd = PaymentDeduction.builder()
                            .monthlyPaymentId(paymentId)
                            .deductionPaymentId(deduction.getId())
                            .deductionType(deduction.getPaymentType())
                            .amount(deduction.getNetAmount())
                            .build();
                    paymentDeductionRepository.save(pd);
                    deduction.setLinkedMonthlyPaymentId(paymentId);
                    deduction.setIsDeduction(true);
                    paymentRepository.save(deduction);
                }
                // h) Create audit log
                createAuditLog(paymentId, null, PaymentStatus.CALCULATED.name(), "MONTHLY_PAYMENT_CALCULATED", "system", null);
                payments.add(payment);
            } catch (Exception ex) {
                log.error("Error calculating payment for supplier {}: {}", supplier.getSupplierId(), ex.getMessage(), ex);
            }
        }
        return payments.stream().map(this::convertToDTO).toList();
    }

    public List<PaymentDTO> getMonthlyPaymentsForApproval(int month, int year, String factoryId) {
        log.info("Fetching monthly payments for approval: month={}, year={}, factoryId={}", month, year, factoryId);
        List<Payment> payments = paymentRepository.findByPaymentTypeAndPeriodMonthAndPeriodYearAndStatus(
                PaymentType.MONTHLY, month, year, PaymentStatus.CALCULATED);
        // Filter by supplier's factoryId
        payments = payments.stream()
                .filter(p -> {
                    Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
                    return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
                })
                .toList();
        return payments.stream().map(payment -> {
            PaymentDTO dto = convertToDTO(payment);
            // Set deduction details
            List<PaymentDeduction> deductions = paymentDeductionRepository.findByMonthlyPaymentId(payment.getId());
            List<DeductionDetailDTO> deductionDTOs = deductions.stream().map(pd -> DeductionDetailDTO.builder()
                    .paymentId(pd.getDeductionPaymentId())
                    .deductionType(pd.getDeductionType())
                    .amount(pd.getAmount())
                    .approvedDate(payment.getApprovedAt())
                    .build()).toList();
            dto.setDeductionAmount(payment.getDeductionAmount());
            // Optionally, set deduction breakdown if needed in DTO
            // dto.setDeductions(deductionDTOs);
            return dto;
        }).toList();
    }

    @Transactional
    public List<PaymentDTO> approveMonthlyPayments(PaymentApprovalRequestDTO request) {
        if (request.getPaymentIds() == null || request.getPaymentIds().isEmpty()) {
            log.warn("No payment IDs provided for approval");
            throw new IllegalArgumentException("No payment IDs provided for approval");
        }
        if (request.getApprovedBy() == null || request.getApprovedBy().isBlank()) {
            log.warn("No approvedBy provided for approval");
            throw new IllegalArgumentException("No approvedBy provided");
        }
        List<PaymentDTO> approvedPayments = new java.util.ArrayList<>();
        for (String paymentId : request.getPaymentIds()) {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new PaymentNotFoundException(paymentId));
            if (payment.getStatus() != PaymentStatus.CALCULATED) {
                log.warn("Payment {} is not in CALCULATED status", paymentId);
                throw new InvalidPaymentStatusException(payment.getStatus().name(), PaymentStatus.APPROVED.name());
            }
            payment.setStatus(PaymentStatus.APPROVED);
            payment.setApprovedAt(java.time.LocalDateTime.now());
            payment.setApprovedBy(request.getApprovedBy());
            paymentRepository.save(payment);
            createAuditLog(paymentId, PaymentStatus.CALCULATED.name(), PaymentStatus.APPROVED.name(),
                    "MONTHLY_PAYMENT_APPROVED", request.getApprovedBy(), request.getNotes());
            approvedPayments.add(convertToDTO(payment));
        }
        log.info("Approved {} monthly payments by {}", approvedPayments.size(), request.getApprovedBy());
        return approvedPayments;
    }

    @Transactional
    public PaymentDTO createAdhocPayment(PaymentDTO paymentDTO) {
        log.info("Creating ad-hoc payment for supplier {} of type {}", paymentDTO.getSupplierId(), paymentDTO.getPaymentType());
        // Validation
        if (paymentDTO.getSupplierId() == null || paymentDTO.getSupplierId().isBlank())
            throw new IllegalArgumentException("Supplier ID is required");
        if (paymentDTO.getRouteId() == null || paymentDTO.getRouteId().isBlank())
            throw new IllegalArgumentException("Route ID is required");
        if (paymentDTO.getNetAmount() == null || paymentDTO.getNetAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");
        if (paymentDTO.getPaymentType() == null ||
                !(paymentDTO.getPaymentType() == PaymentType.LOAN || paymentDTO.getPaymentType() == PaymentType.ADVANCE || paymentDTO.getPaymentType() == PaymentType.FERTILIZER))
            throw new IllegalArgumentException("Invalid payment type for ad-hoc payment");
        // Validate supplier and route exist
        Supplier supplier = supplierRepository.findById(Long.parseLong(paymentDTO.getSupplierId()))
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found: " + paymentDTO.getSupplierId()));
        Route route = routeRepository.findById(Long.parseLong(paymentDTO.getRouteId()))
                .orElseThrow(() -> new IllegalArgumentException("Route not found: " + paymentDTO.getRouteId()));
        // Create Payment entity
        String paymentId = generatePaymentId(paymentDTO.getPaymentType(), LocalDate.now());
        Payment payment = Payment.builder()
                .id(paymentId)
                .paymentType(paymentDTO.getPaymentType())
                .supplierId(paymentDTO.getSupplierId())
                .routeId(paymentDTO.getRouteId())
                .grossAmount(BigDecimal.ZERO)
                .deductionAmount(BigDecimal.ZERO)
                .netAmount(paymentDTO.getNetAmount())
                .disbursementMethod(paymentDTO.getDisbursementMethod())
                .status(PaymentStatus.PENDING_APPROVAL)
                .isDeduction(false)
                .notes(paymentDTO.getNotes())
                .build();
        paymentRepository.save(payment);
        createAuditLog(paymentId, null, PaymentStatus.PENDING_APPROVAL.name(), "ADHOC_PAYMENT_CALCULATED", "system", null);
        log.info("Created {} payment {} for supplier {}", paymentDTO.getPaymentType(), paymentId, paymentDTO.getSupplierId());
        return convertToDTO(payment);
    }

    @Transactional
    public PaymentDTO approveAdhocPayment(String paymentId, String approvedBy) {
        if (paymentId == null || paymentId.isBlank())
            throw new IllegalArgumentException("Payment ID is required");
        if (approvedBy == null || approvedBy.isBlank())
            throw new IllegalArgumentException("ApprovedBy is required");
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        if (payment.getStatus() != PaymentStatus.PENDING_APPROVAL) {
            log.warn("Payment {} is not in PENDING_APPROVAL status", paymentId);
            throw new InvalidPaymentStatusException(payment.getStatus().name(), PaymentStatus.APPROVED.name());
        }
        payment.setStatus(PaymentStatus.APPROVED);
        payment.setApprovedAt(java.time.LocalDateTime.now());
        payment.setApprovedBy(approvedBy);
        paymentRepository.save(payment);
        createAuditLog(paymentId, PaymentStatus.PENDING_APPROVAL.name(), PaymentStatus.APPROVED.name(),
                "ADHOC_PAYMENT_APPROVED", approvedBy, null);
        log.info("Approved ad-hoc payment {} by {}", paymentId, approvedBy);
        return convertToDTO(payment);
    }

    public List<PaymentDTO> getPendingAdhocPayments(String factoryId, PaymentType type) {
        log.info("Fetching pending ad-hoc payments for factoryId={}, type={}", factoryId, type);
        List<PaymentType> types = List.of(PaymentType.LOAN, PaymentType.ADVANCE, PaymentType.FERTILIZER);
        List<Payment> payments = paymentRepository.findByPaymentTypeInAndStatus(
                type != null ? List.of(type) : types, PaymentStatus.APPROVED);
        // Filter by supplier's factoryId
        payments = payments.stream()
                .filter(p -> {
                    Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
                    return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
                })
                .sorted((a, b) -> {
                    if (a.getApprovedAt() == null || b.getApprovedAt() == null) return 0;
                    return a.getApprovedAt().compareTo(b.getApprovedAt());
                })
                .toList();
        return payments.stream().map(this::convertToDTO).toList();
    }

    public List<PaymentDTO> getBankPaymentsQueue(String factoryId) {
        List<Payment> payments = paymentRepository.findByStatusAndDisbursementMethod(PaymentStatus.APPROVED, DisbursementMethod.BANK);
        // Filter by supplier's factoryId
        payments = payments.stream()
                .filter(p -> {
                    Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
                    return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
                })
                .sorted((a, b) -> {
                    if (a.getApprovedAt() == null || b.getApprovedAt() == null) return 0;
                    return a.getApprovedAt().compareTo(b.getApprovedAt());
                })
                .toList();
        return payments.stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public BankCsvBatchDTO generateBankCsv(BankCsvGenerationRequestDTO request) {
        log.info("Generating bank CSV for factoryId={}, by {}", request.getFactoryId(), request.getGeneratedBy());
        if (request.getPaymentIds() == null || request.getPaymentIds().isEmpty())
            throw new IllegalArgumentException("No payment IDs provided for CSV generation");
        if (request.getGeneratedBy() == null || request.getGeneratedBy().isBlank())
            throw new IllegalArgumentException("GeneratedBy is required");
        // Validate and collect payments
        List<Payment> payments = request.getPaymentIds().stream().map(pid -> {
            Payment p = paymentRepository.findById(pid)
                    .orElseThrow(() -> new PaymentNotFoundException(pid));
            if (p.getStatus() != PaymentStatus.APPROVED)
                throw new InvalidPaymentStatusException(p.getStatus().name(), PaymentStatus.APPROVED.name());
            if (p.getDisbursementMethod() != DisbursementMethod.BANK)
                throw new IllegalArgumentException("Payment " + pid + " is not set for BANK disbursement");
            if (bankCsvPaymentRepository.existsByPaymentId(pid))
                throw new IllegalArgumentException("Payment " + pid + " is already in a bank batch");
            return p;
        }).toList();
        // Generate batch ID
        String batchId = generateBatchId("CSV", LocalDate.now());
        BigDecimal totalAmount = payments.stream().map(Payment::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        // Create BankCsvBatch
        BankCsvBatch batch = BankCsvBatch.builder()
                .id(batchId)
                .factoryId(request.getFactoryId())
                .totalPayments(payments.size())
                .totalAmount(totalAmount)
                .status("GENERATED")
                .generatedBy(request.getGeneratedBy())
                .build();
        bankCsvBatchRepository.save(batch);
        // Link payments to batch
        for (Payment payment : payments) {
            BankCsvPayment link = BankCsvPayment.builder()
                    .batchId(batchId)
                    .paymentId(payment.getId())
                    .build();
            bankCsvPaymentRepository.save(link);
            payment.setStatus(PaymentStatus.PROCESSING);
            payment.setBatchId(batchId);
            paymentRepository.save(payment);
            createAuditLog(payment.getId(), PaymentStatus.APPROVED.name(), PaymentStatus.PROCESSING.name(),
                    "BANK_CSV_PROCESSING", request.getGeneratedBy(), null);
        }
        // Generate CSV file content (simulate file path)
        String fileName = batchId + ".csv";
        String filePath = "/bank-csv/" + fileName;
        batch.setFileName(fileName);
        batch.setFilePath(filePath);
        bankCsvBatchRepository.save(batch);
        // Build DTO
        List<PaymentDTO> paymentDTOs = payments.stream().map(this::convertToDTO).toList();
        BankCsvBatchDTO dto = modelMapper.map(batch, BankCsvBatchDTO.class);
        dto.setPayments(paymentDTOs);
        dto.setTotalCount(payments.size());
        dto.setTotalAmount(totalAmount);
        log.info("Generated bank CSV {} with {} payments totaling Rs. {}", batchId, payments.size(), totalAmount);
        return dto;
    }

    @Transactional
    public CashCollectionBatch disburseCash(CashDisbursementRequestDTO request) {
        log.info("Disbursing cash for routeId={}, driverId={}, by {}", request.getRouteId(), request.getDriverId(), request.getCollectedBy());
        if (request.getPaymentIds() == null || request.getPaymentIds().isEmpty())
            throw new IllegalArgumentException("No payment IDs provided for cash disbursement");
        if (request.getCollectedBy() == null || request.getCollectedBy().isBlank())
            throw new IllegalArgumentException("CollectedBy is required");
        // Validate and collect payments
        List<Payment> payments = request.getPaymentIds().stream().map(pid -> {
            Payment p = paymentRepository.findById(pid)
                    .orElseThrow(() -> new PaymentNotFoundException(pid));
            if (p.getStatus() != PaymentStatus.APPROVED && p.getStatus() != PaymentStatus.READY_FOR_DISBURSEMENT)
                throw new InvalidPaymentStatusException(p.getStatus().name(), PaymentStatus.DISBURSED.name());
            if (p.getDisbursementMethod() != DisbursementMethod.CASH)
                throw new IllegalArgumentException("Payment " + pid + " is not set for CASH disbursement");
            if (!request.getRouteId().equals(p.getRouteId()))
                throw new IllegalArgumentException("Payment " + pid + " is not for the specified route");
            return p;
        }).toList();
        // Check total amount
        BigDecimal actualTotal = payments.stream().map(Payment::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (actualTotal.compareTo(request.getTotalAmount()) != 0) {
            throw new IllegalArgumentException("Total amount mismatch: " + request.getTotalAmount() + ", actual " + actualTotal);
        }
        // Get factoryId from first payment's supplier
        String factoryId = "1"; // default
        if (!payments.isEmpty()) {
            Supplier s = supplierRepository.findById(Long.parseLong(payments.get(0).getSupplierId())).orElse(null);
            if (s != null) factoryId = String.valueOf(s.getFactory().getFactoryId());
        }
        // Generate batch ID
        String batchId = generateBatchId("CASH", LocalDate.now());
        // Create CashCollectionBatch
        CashCollectionBatch batch = CashCollectionBatch.builder()
                .id(batchId)
                .routeId(request.getRouteId())
                .driverId(request.getDriverId())
                .factoryId(factoryId)
                .totalPayments(payments.size())
                .totalAmount(actualTotal)
                .collectedBy(request.getCollectedBy())
                .notes(request.getNotes())
                .build();
        cashCollectionBatchRepository.save(batch);
        // Link payments to batch
        for (Payment payment : payments) {
            CashCollectionPayment link = CashCollectionPayment.builder()
                    .batchId(batchId)
                    .paymentId(payment.getId())
                    .build();
            cashCollectionPaymentRepository.save(link);
            payment.setStatus(PaymentStatus.DISBURSED);
            payment.setDisbursedAt(LocalDateTime.now());
            payment.setDisbursedBy(request.getCollectedBy());
            paymentRepository.save(payment);
            createAuditLog(payment.getId(), payment.getStatus().name(), PaymentStatus.DISBURSED.name(), "CASH_DISBURSED", request.getCollectedBy(), null);
        }
        log.info("Disbursed cash batch {} with {} payments totaling Rs. {}", batchId, payments.size(), actualTotal);
        return batch;
    }

    // CSV generation: build CSV content for a batch
    public String generateCsvContent(String batchId) {
        BankCsvBatch batch = bankCsvBatchRepository.findById(batchId)
                .orElseThrow(() -> new CsvGenerationException("Batch not found: " + batchId));
        List<BankCsvPayment> links = bankCsvPaymentRepository.findByBatchId(batchId);
        if (links == null || links.isEmpty()) throw new CsvGenerationException("No payments linked to batch: " + batchId);
        StringBuilder sb = new StringBuilder();
        sb.append("Supplier ID,Supplier Name,Account Number,Bank Name,Branch,Amount,Reference,Date\n");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (BankCsvPayment link : links) {
            Payment p = paymentRepository.findById(link.getPaymentId()).orElse(null);
            if (p == null) continue;
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            if (s == null) throw new CsvGenerationException("Supplier not found for payment: " + p.getId());
            // Validate bank details (assume Supplier has getters for bankAccount etc.)
            String account = Optional.ofNullable(s.getBankAccountNumber()).orElse("");
            String bank = Optional.ofNullable(s.getBankName()).orElse("");
            String branch = Optional.ofNullable(s.getBankBranch()).orElse("");
            if (account.isBlank() || bank.isBlank()) throw new CsvGenerationException("Missing bank details for supplier: " + s.getSupplierId());
            String supplierNameEscaped = s.getUser().getName().replaceAll(",", " ");
            String amount = p.getNetAmount() == null ? "0.00" : p.getNetAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            String date = p.getCreatedAt() == null ? LocalDate.now().format(df) : p.getCreatedAt().toLocalDate().format(df);
            sb.append(String.join(",", Arrays.asList(String.valueOf(s.getSupplierId()), supplierNameEscaped, account, bank, branch, amount, p.getId(), date)));
            sb.append('\n');
        }
        return sb.toString();
    }

    // Download CSV bytes (reads filePath). If file not present, throw exception.
    public byte[] downloadBankCsv(String batchId) {
        BankCsvBatch batch = bankCsvBatchRepository.findById(batchId)
                .orElseThrow(() -> new CsvGenerationException("Batch not found: " + batchId));
        if (batch.getFilePath() == null) throw new CsvGenerationException("No file path for batch: " + batchId);
        try {
            Path p = Path.of(batch.getFilePath());
            if (!Files.exists(p)) throw new CsvGenerationException("CSV file not found: " + batch.getFilePath());
            return Files.readAllBytes(p);
        } catch (IOException ex) {
            throw new CsvGenerationException("Failed to read CSV file: " + batch.getFilePath(), ex);
        }
    }

    // Calculate total approved deductions for supplier
    public BigDecimal calculateDeductions(String supplierId) {
        List<Payment> pending = paymentRepository.findPendingDeductionsForSupplier(supplierId);
        return pending.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<DeductionDetailDTO> getDeductionBreakdown(String supplierId) {
        List<Payment> pending = paymentRepository.findPendingDeductionsForSupplier(supplierId);
        return pending.stream().map(p -> DeductionDetailDTO.builder()
                .paymentId(p.getId())
                .deductionType(p.getPaymentType())
                .amount(Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO))
                .approvedDate(p.getApprovedAt())
                .build()).toList();
    }

    @Transactional
    public PaymentDTO updatePaymentStatus(String paymentId, PaymentStatus newStatus, String updatedBy) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        PaymentStatus old = payment.getStatus();
        if (!isValidStatusTransition(old, newStatus)) {
            throw new InvalidPaymentStatusException(old == null ? "null" : old.name(), newStatus.name());
        }
        payment.setStatus(newStatus);
        payment.setUpdatedAt(LocalDateTime.now());
        if (newStatus == PaymentStatus.APPROVED) {
            payment.setApprovedAt(LocalDateTime.now());
            payment.setApprovedBy(updatedBy);
        }
        if (newStatus == PaymentStatus.DISBURSED) {
            payment.setDisbursedAt(LocalDateTime.now());
            payment.setDisbursedBy(updatedBy);
        }
        paymentRepository.save(payment);
        createAuditLog(paymentId, old == null ? null : old.name(), newStatus.name(), "STATUS_UPDATED", updatedBy, null);
        return convertToDTO(payment);
    }

    @Transactional
    public PaymentDTO cancelPayment(String paymentId, String cancelledBy, String reason) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        if (payment.getStatus() == PaymentStatus.DISBURSED) throw new PaymentAlreadyProcessedException(paymentId);
        PaymentStatus old = payment.getStatus();
        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setNotes(Optional.ofNullable(payment.getNotes()).map(n -> n + "\nCancelled: " + reason).orElse("Cancelled: " + reason));
        paymentRepository.save(payment);
        createAuditLog(paymentId, old == null ? null : old.name(), PaymentStatus.CANCELLED.name(), "PAYMENT_CANCELLED", cancelledBy, reason);
        return convertToDTO(payment);
    }

    // Cash queue grouped by route
    public Map<String, CashCollectionDTO> getCashPaymentsQueue(String factoryId) {
        List<Payment> payments = paymentRepository.findByStatusAndDisbursementMethod(PaymentStatus.APPROVED, DisbursementMethod.CASH);
        Map<String, List<Payment>> byRoute = payments.stream().filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).collect(Collectors.groupingBy(Payment::getRouteId));
        Map<String, CashCollectionDTO> result = new HashMap<>();
        for (Map.Entry<String, List<Payment>> e : byRoute.entrySet()) {
            String routeId = e.getKey();
            List<Payment> list = e.getValue();
            List<PaymentDTO> monthly = list.stream().filter(p -> p.getPaymentType() == PaymentType.MONTHLY).map(this::convertToDTO).toList();
            List<PaymentDTO> adhoc = list.stream().filter(p -> p.getPaymentType() != PaymentType.MONTHLY).map(this::convertToDTO).toList();
            BigDecimal total = list.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            Route route = routeRepository.findById(Long.parseLong(routeId)).orElse(null);
            CashCollectionDTO dto = CashCollectionDTO.builder()
                    .routeId(routeId)
                    .routeName(route == null ? null : route.getName())
                    .monthlyPayments(monthly)
                    .adhocPayments(adhoc)
                    .totalAmount(total)
                    .build();
            result.put(routeId, dto);
        }
        return result;
    }

    public CashCollectionDTO getCashPaymentsByRoute(String routeId) {
        List<Payment> payments = paymentRepository.findByRouteIdAndDisbursementMethodAndStatusIn(routeId, DisbursementMethod.CASH, List.of(PaymentStatus.APPROVED, PaymentStatus.READY_FOR_DISBURSEMENT));
        List<PaymentDTO> monthly = payments.stream().filter(p -> p.getPaymentType() == PaymentType.MONTHLY).map(this::convertToDTO).toList();
        List<PaymentDTO> adhoc = payments.stream().filter(p -> p.getPaymentType() != PaymentType.MONTHLY).map(this::convertToDTO).toList();
        BigDecimal total = payments.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        Route route = routeRepository.findById(Long.parseLong(routeId)).orElse(null);
        return CashCollectionDTO.builder()
                .routeId(routeId)
                .routeName(route == null ? null : route.getName())
                .monthlyPayments(monthly)
                .adhocPayments(adhoc)
                .totalAmount(total)
                .build();
    }

    @Transactional
    public void markCashPaymentsReady(String routeId) {
        List<Payment> payments = paymentRepository.findByRouteIdAndDisbursementMethodAndStatusIn(routeId, DisbursementMethod.CASH, List.of(PaymentStatus.APPROVED));
        for (Payment p : payments) {
            p.setStatus(PaymentStatus.READY_FOR_DISBURSEMENT);
            paymentRepository.save(p);
            createAuditLog(p.getId(), PaymentStatus.APPROVED.name(), PaymentStatus.READY_FOR_DISBURSEMENT.name(), "MARK_READY_FOR_CASH", "system", "Route ready");
        }
    }

    public List<PaymentSummaryDTO> getPaymentSummaryByRoute(int month, int year, String factoryId) {
        List<Payment> payments = paymentRepository.findByPeriodMonthAndPeriodYear(month, year);
        // filter factory
        payments = payments.stream().filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).collect(Collectors.toList());
        Map<String, List<Payment>> byRoute = payments.stream().collect(Collectors.groupingBy(Payment::getRouteId));
        List<PaymentSummaryDTO> summaries = new ArrayList<>();
        for (Map.Entry<String, List<Payment>> e : byRoute.entrySet()) {
            String routeId = e.getKey();
            List<Payment> list = e.getValue();
            int totalPayments = list.size();
            BigDecimal totalAmount = list.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal bankAmount = list.stream().filter(p -> p.getDisbursementMethod() == DisbursementMethod.BANK).map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal cashAmount = list.stream().filter(p -> p.getDisbursementMethod() == DisbursementMethod.CASH).map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            int supplierCount = (int) list.stream().map(Payment::getSupplierId).distinct().count();
            Route route = routeRepository.findById(Long.parseLong(routeId)).orElse(null);
            summaries.add(PaymentSummaryDTO.builder()
                    .routeId(routeId)
                    .routeName(route == null ? null : route.getName())
                    .totalPayments(totalPayments)
                    .totalAmount(totalAmount)
                    .bankAmount(bankAmount)
                    .cashAmount(cashAmount)
                    .supplierCount(supplierCount)
                    .build());
        }
        return summaries;
    }

    public Map<String, Object> getDashboardStatistics(int month, int year, String factoryId) {
        Map<String, Object> stats = new HashMap<>();
        // Monthly pending (CALCULATED)
        List<Payment> monthlyPending = paymentRepository.findByPaymentTypeAndPeriodMonthAndPeriodYearAndStatus(PaymentType.MONTHLY, month, year, PaymentStatus.CALCULATED);
        monthlyPending = monthlyPending.stream().filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).toList();
        BigDecimal monthlyPendingSum = monthlyPending.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("monthlyPendingCount", monthlyPending.size());
        stats.put("monthlyPendingSum", monthlyPendingSum);
        // Adhoc approved
        List<Payment> adhocApproved = paymentRepository.findByPaymentTypeInAndStatus(List.of(PaymentType.LOAN, PaymentType.ADVANCE, PaymentType.FERTILIZER), PaymentStatus.APPROVED);
        adhocApproved = adhocApproved.stream().filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).toList();
        BigDecimal adhocSum = adhocApproved.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("adhocApprovedCount", adhocApproved.size());
        stats.put("adhocApprovedSum", adhocSum);
        // Cash ready
        List<Payment> cashReady = paymentRepository.findByStatusAndDisbursementMethod(PaymentStatus.APPROVED, DisbursementMethod.CASH).stream().filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).toList();
        BigDecimal cashReadySum = cashReady.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("cashReadySum", cashReadySum);
        // Bank queue
        List<Payment> bankQueue = paymentRepository.findByStatusAndDisbursementMethod(PaymentStatus.APPROVED, DisbursementMethod.BANK).stream().filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).toList();
        BigDecimal bankQueueSum = bankQueue.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("bankQueueSum", bankQueueSum);
        // Payments by route (count)
        Map<String, Long> byRouteCount = paymentRepository.findByPeriodMonthAndPeriodYear(month, year).stream().filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).collect(Collectors.groupingBy(Payment::getRouteId, Collectors.counting()));
        stats.put("paymentsByRoute", byRouteCount);
        // Total disbursed this month
        BigDecimal disbursedSum = paymentRepository.findByPeriodMonthAndPeriodYear(month, year).stream().filter(p -> p.getStatus() == PaymentStatus.DISBURSED).filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("disbursedThisMonth", disbursedSum);
        return stats;
    }

    public List<PaymentDTO> getOverduePayments(String factoryId) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        List<Payment> payments = paymentRepository.findByStatusAndDisbursementMethod(PaymentStatus.APPROVED, DisbursementMethod.CASH);
        payments = payments.stream().filter(p -> p.getCreatedAt() != null && p.getCreatedAt().isBefore(cutoff)).filter(p -> {
            Supplier s = supplierRepository.findById(Long.parseLong(p.getSupplierId())).orElse(null);
            return s != null && factoryId.equals(String.valueOf(s.getFactory().getFactoryId()));
        }).toList();
        return payments.stream().map(this::convertToDTO).toList();
    }

    public List<PaymentDTO> getUnlinkedDeductions(String supplierId) {
        List<Payment> pending = paymentRepository.findPendingDeductionsForSupplier(supplierId);
        return pending.stream().map(this::convertToDTO).toList();
    }

    public BigDecimal calculateMonthlyGrossTotal(int month, int year, String routeId) {
        List<Payment> payments = paymentRepository.findByPeriodMonthAndPeriodYear(month, year).stream().filter(p -> routeId.equals(p.getRouteId())).toList();
        return payments.stream().map(p -> Optional.ofNullable(p.getGrossAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateMonthlyNetTotal(int month, int year, String routeId) {
        List<Payment> payments = paymentRepository.findByPeriodMonthAndPeriodYear(month, year).stream().filter(p -> routeId.equals(p.getRouteId())).toList();
        return payments.stream().map(p -> Optional.ofNullable(p.getNetAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // helper: valid status transition
    private boolean isValidStatusTransition(PaymentStatus from, PaymentStatus to) {
        if (from == null) return true;
        switch (from) {
            case CALCULATED:
                return to == PaymentStatus.APPROVED || to == PaymentStatus.CANCELLED;
            case PENDING_APPROVAL:
                return to == PaymentStatus.APPROVED || to == PaymentStatus.CANCELLED;
            case APPROVED:
                return to == PaymentStatus.READY_FOR_DISBURSEMENT || to == PaymentStatus.PROCESSING || to == PaymentStatus.CANCELLED || to == PaymentStatus.DISBURSED;
            case READY_FOR_DISBURSEMENT:
                return to == PaymentStatus.PROCESSING || to == PaymentStatus.DISBURSED || to == PaymentStatus.CANCELLED;
            case PROCESSING:
                return to == PaymentStatus.DISBURSED || to == PaymentStatus.FAILED || to == PaymentStatus.CANCELLED;
            default:
                return false;
        }
    }

    // Helper: Generate Payment ID
    private String generatePaymentId(PaymentType type, LocalDate date) {
        String prefix = "PAY";
        String dateStr = date.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMM"));
        String idPrefix = prefix + "-" + dateStr;
        Payment last = paymentRepository.findTopByIdStartingWithOrderByIdDesc(idPrefix);
        int sequence = 1;
        if (last != null) {
            String seqStr = last.getId().substring(last.getId().lastIndexOf("-") + 1);
            try { sequence = Integer.parseInt(seqStr) + 1; } catch (Exception ignored) {}
        }
        return String.format("%s-%s-%04d", prefix, dateStr, sequence);
    }

    // Helper: Generate Batch ID
    private String generateBatchId(String prefix, LocalDate date) {
        String dateStr = date.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String idPrefix = prefix + "-" + dateStr;
        BankCsvBatch last = bankCsvBatchRepository.findTopByOrderByBatchNumberDesc();
        long sequence = 1;
        if (last != null && last.getId().startsWith(idPrefix)) {
            String seqStr = last.getId().substring(last.getId().lastIndexOf("-") + 1);
            try { sequence = Long.parseLong(seqStr) + 1; } catch (Exception ignored) {}
        }
        return String.format("%s-%s-%03d", prefix, dateStr, sequence);
    }

    // Helper: Create Audit Log
    private void createAuditLog(String paymentId, String oldStatus, String newStatus, String action, String changedBy, String notes) {
        PaymentAuditLog logEntry = PaymentAuditLog.builder()
                .paymentId(paymentId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .action(action)
                .changedBy(changedBy)
                .notes(notes)
                .changedAt(java.time.LocalDateTime.now())
                .build();
        paymentAuditLogRepository.save(logEntry);
        log.info("Audit log created: Payment {} - {} -> {} by {}", paymentId, oldStatus, newStatus, changedBy);
    }

    // Helper: Convert to DTO
    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = modelMapper.map(payment, PaymentDTO.class);
        // Fetch and set supplier name
        Supplier supplier = supplierRepository.findById(Long.parseLong(payment.getSupplierId())).orElse(null);
        if (supplier != null) dto.setSupplierName(supplier.getUser().getName());
        // Fetch and set route name (routeId stored as String on Payment, Route entity uses Long ids)
        Route route = resolveRouteByStringId(payment.getRouteId());
        if (route != null) dto.setRouteName(route.getName());
        return dto;
    }

    public List<BankCsvBatchDTO> getBankCsvHistory(String factoryId, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime from = fromDate == null ? LocalDateTime.MIN : fromDate.atStartOfDay();
        LocalDateTime to = toDate == null ? LocalDateTime.now() : toDate.atTime(23, 59, 59);
        List<BankCsvBatch> batches = bankCsvBatchRepository.findByGeneratedAtBetween(from, to);
        return batches.stream().filter(b -> factoryId == null || factoryId.equals(b.getFactoryId()))
                .map(b -> {
                    BankCsvBatchDTO dto = modelMapper.map(b, BankCsvBatchDTO.class);
                    List<BankCsvPayment> links = bankCsvPaymentRepository.findByBatchId(b.getId());
                    List<PaymentDTO> payments = links.stream().map(l -> paymentRepository.findById(l.getPaymentId()).map(this::convertToDTO).orElse(null)).filter(Objects::nonNull).toList();
                    dto.setPayments(payments);
                    dto.setTotalCount(b.getTotalPayments());
                    dto.setTotalAmount(b.getTotalAmount());
                    return dto;
                }).toList();
    }

    public List<CashCollectionBatch> getCashCollectionHistory(String routeId, LocalDateTime from, LocalDateTime to) {
        return cashCollectionBatchRepository.findByRouteIdAndCollectedAtBetween(routeId, from, to);
    }

    public Optional<PaymentDTO> getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId).map(this::convertToDTO);
    }

    public List<PaymentDTO> getPaymentsBySupplier(String supplierId, PaymentFilterDTO filter) {
        List<Payment> payments = paymentRepository.findBySupplierId(supplierId);
        // Apply filters
        if (filter.getPeriodMonth() != null) {
            payments = payments.stream().filter(p -> p.getPeriodMonth() == filter.getPeriodMonth()).toList();
        }
        if (filter.getPeriodYear() != null) {
            payments = payments.stream().filter(p -> p.getPeriodYear() == filter.getPeriodYear()).toList();
        }
        if (filter.getRouteId() != null) {
            payments = payments.stream().filter(p -> filter.getRouteId().equals(p.getRouteId())).toList();
        }
        if (filter.getPaymentType() != null) {
            payments = payments.stream().filter(p -> p.getPaymentType() == filter.getPaymentType()).toList();
        }
        if (filter.getStatus() != null) {
            payments = payments.stream().filter(p -> p.getStatus() == filter.getStatus()).toList();
        }
        if (filter.getDisbursementMethod() != null) {
            payments = payments.stream().filter(p -> p.getDisbursementMethod() == filter.getDisbursementMethod()).toList();
        }
        if (filter.getFromDate() != null) {
            payments = payments.stream().filter(p -> p.getCreatedAt() != null && p.getCreatedAt().toLocalDate().isAfter(filter.getFromDate().minusDays(1))).toList();
        }
        if (filter.getToDate() != null) {
            payments = payments.stream().filter(p -> p.getCreatedAt() != null && p.getCreatedAt().toLocalDate().isBefore(filter.getToDate().plusDays(1))).toList();
        }
        return payments.stream().map(this::convertToDTO).toList();
    }

    public Page<PaymentDTO> getPaymentHistory(PaymentFilterDTO filter, Pageable pageable) {
        List<Payment> allPayments = paymentRepository.findAll();
        List<Payment> payments = allPayments.stream()
                .filter(p -> filter.getSupplierId() == null || filter.getSupplierId().equals(p.getSupplierId()))
                .filter(p -> filter.getRouteId() == null || filter.getRouteId().equals(p.getRouteId()))
                .filter(p -> filter.getPeriodMonth() == null || p.getPeriodMonth() == filter.getPeriodMonth())
                .filter(p -> filter.getPeriodYear() == null || p.getPeriodYear() == filter.getPeriodYear())
                .filter(p -> filter.getPaymentType() == null || p.getPaymentType() == filter.getPaymentType())
                .filter(p -> filter.getStatus() == null || p.getStatus() == filter.getStatus())
                .filter(p -> filter.getDisbursementMethod() == null || p.getDisbursementMethod() == filter.getDisbursementMethod())
                .filter(p -> filter.getFromDate() == null || (p.getCreatedAt() != null && p.getCreatedAt().toLocalDate().isAfter(filter.getFromDate().minusDays(1))))
                .filter(p -> filter.getToDate() == null || (p.getCreatedAt() != null && p.getCreatedAt().toLocalDate().isBefore(filter.getToDate().plusDays(1))))
                .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), payments.size());
        List<PaymentDTO> content = payments.subList(start, end).stream().map(this::convertToDTO).toList();
        return new PageImpl<>(content, pageable, payments.size());
    }
}
