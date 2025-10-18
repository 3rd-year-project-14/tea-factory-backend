package com.teafactory.pureleaf.advanceProcess.service;

import com.teafactory.pureleaf.advanceProcess.dto.AdvanceApprovalDto;
import com.teafactory.pureleaf.advanceProcess.dto.AdvanceDetailsDto;
import com.teafactory.pureleaf.advanceProcess.dto.AdvanceRequestDto;
import com.teafactory.pureleaf.advanceProcess.dto.AdvanceResponseDto;
import com.teafactory.pureleaf.advanceProcess.dto.AdvanceStatusCountDto;
import com.teafactory.pureleaf.advanceProcess.dto.AdvanceRejectionDto;
import com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance;
import com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance.Status;
import com.teafactory.pureleaf.advanceProcess.repository.SupplierAdvanceRepository;
import com.teafactory.pureleaf.advanceProcess.spec.AdvanceSpecification;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SupplierAdvanceService {
    private final SupplierAdvanceRepository advanceRepository;
    private final SupplierRepository supplierRepository;
    private final EligibilityService eligibilityService;
    private final BagWeightRepository bagWeightRepository;

    public AdvanceResponseDto createAdvanceRequest(AdvanceRequestDto requestDto) {
        Supplier supplier = supplierRepository.findById(requestDto.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));
        eligibilityService.checkEligibility(requestDto.getSupplierId(), requestDto.getRequestedAmount());
        SupplierAdvance advance = SupplierAdvance.builder()
                .supplier(supplier)
                .requestedDate(LocalDate.now())
                .requestedAmount(requestDto.getRequestedAmount())
                .status(Status.REQUESTED)
                .purpose(requestDto.getPurpose())
                .paymentMethod(SupplierAdvance.PaymentMethod.valueOf(requestDto.getPaymentMethod().name()))
                .build();
        advanceRepository.save(advance);
        return toResponseDto(advance);
    }

    public List<AdvanceResponseDto> getAdvanceHistoryForSupplier(Long supplierId) {
        return advanceRepository.findBySupplier_SupplierIdOrderByRequestedDateDesc(supplierId)
                .stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    public AdvanceDetailsDto getAdvanceById(Long id) {
        SupplierAdvance advance = advanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advance not found with id: " + id));

        AdvanceDetailsDto.AdvanceDetailsDtoBuilder builder = AdvanceDetailsDto.builder()
                .id(advance.getId())
                .supplierName(advance.getSupplier().getUser().getName())
                .requestedDate(advance.getRequestedDate())
                .requestedAmount(advance.getRequestedAmount())
                .approvedAmount(advance.getApprovedAmount())
                .purpose(advance.getPurpose())
                .status(advance.getStatus())
                .paymentMethod(advance.getPaymentMethod())
                .rejectionReason(advance.getRejectionReason());

        if (advance.getStatus() == SupplierAdvance.Status.REQUESTED) {
            Long supplierId = advance.getSupplier().getSupplierId();

            // Get this month's weight
            LocalDate today = LocalDate.now();
            BigDecimal thisMonthWeight = bagWeightRepository.findTotalNetWeightBySupplierIdAndMonth(supplierId, today.getMonthValue(), today.getYear());
            builder.thisMonthWeight(thisMonthWeight);

            // Perform eligibility check
            EligibilityResult eligibility = eligibilityService.checkEligibility(supplierId, advance.getRequestedAmount());
            builder.eligibilityStatus(eligibility.eligible() ? "PASS" : "FAIL");
            builder.eligibilityFailReasons(eligibility.validationErrors());
        }

        return builder.build();
    }

    public List<AdvanceStatusCountDto> getAdvanceStatusCounts(Long factoryId, Integer month, Integer year) {
        int queryYear = (year != null) ? year : LocalDate.now().getYear();
        int queryMonth = (month != null) ? month : LocalDate.now().getMonthValue();
        return advanceRepository.countAdvancesByStatus(factoryId, queryYear, queryMonth);
    }

    public List<AdvanceResponseDto> getPendingAdvancesForFactory(Long factoryId) {
        // Assuming SupplierAdvance has supplier.factory.id
        return advanceRepository.findByStatusOrderByRequestedDateAsc(Status.REQUESTED)
                .stream().filter(a -> a.getSupplier().getFactory().getFactoryId().equals(factoryId))
                .map(this::toResponseDto).collect(Collectors.toList());
    }

    public AdvanceResponseDto approveOrRejectAdvance(Long id, AdvanceApprovalDto approvalDto, Long approvedByUserId) {
        return processAdvanceApproval(id, approvalDto, approvedByUserId);
    }

    public AdvanceResponseDto rejectAdvance(Long id, AdvanceRejectionDto rejectionDto) {
        SupplierAdvance advance = advanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advance not found with id: " + id));

        if (advance.getStatus() != Status.REQUESTED) {
            throw new IllegalStateException("Only advances with REQUESTED status can be rejected.");
        }

        advance.setStatus(Status.REJECTED);
        advance.setRejectionReason(rejectionDto.getRejectionReason());
        advance.setApprovedBy(rejectionDto.getRejectedByUserId()); // Storing the ID of the manager who rejected
        advance.setApprovedAt(LocalDateTime.now()); // Timestamp of the rejection action

        advanceRepository.save(advance);

        return toResponseDto(advance);
    }

    public AdvanceResponseDto markAdvanceAsPaid(Long id) {
        SupplierAdvance advance = advanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advance not found"));
        advance.setStatus(Status.PAID);
        advance.setPaidDate(LocalDate.now());
        advanceRepository.save(advance);
        return toResponseDto(advance);
    }

    public boolean isManager(Authentication authentication) {
        // Stub: check if user has manager role
        // Replace with your actual logic
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        // Stub: extract userId from authentication principal
        // Replace with your actual logic
        return Long.valueOf(authentication.getName());
    }

    public AdvanceResponseDto processAdvanceApproval(Long advanceId, AdvanceApprovalDto approvalDto, Long approvedByUserId) {
        SupplierAdvance advance = advanceRepository.findById(advanceId)
                .orElseThrow(() -> new EntityNotFoundException("Advance not found"));
        if (advance.getStatus() != Status.REQUESTED) {
            throw new IllegalStateException("Advance is not in REQUESTED status");
        }
        if (approvalDto.getAction() == AdvanceApprovalDto.Action.APPROVE) {
            advance.setApprovedAmount(approvalDto.getApprovedAmount());
            advance.setApprovedBy(approvedByUserId); // can be null if not tracked
            advance.setApprovedAt(LocalDateTime.now());
            advance.setStatus(Status.APPROVED);
        } else if (approvalDto.getAction() == AdvanceApprovalDto.Action.REJECT) {
            if (approvalDto.getRejectionReason() == null || approvalDto.getRejectionReason().isEmpty()) {
                throw new IllegalArgumentException("Rejection reason required");
            }
            advance.setRejectionReason(approvalDto.getRejectionReason());
            advance.setStatus(Status.REJECTED);
        }
        advanceRepository.save(advance);
        return toResponseDto(advance);
    }

    public void checkEligibility(Long supplierId, BigDecimal requestedAmount) {
        eligibilityService.checkEligibility(supplierId, requestedAmount);
    }

    private AdvanceResponseDto toResponseDto(SupplierAdvance advance) {
        return AdvanceResponseDto.builder()
                .id(advance.getId())
                .supplierName(advance.getSupplier().getUser().getName())
                .requestedDate(advance.getRequestedDate())
                .requestedAmount(advance.getRequestedAmount())
                .approvedAmount(advance.getApprovedAmount())
                .purpose(advance.getPurpose())
                .status(AdvanceResponseDto.Status.valueOf(advance.getStatus().name()))
                .paymentMethod(AdvanceResponseDto.PaymentMethod.valueOf(advance.getPaymentMethod().name()))
                .rejectionReason(advance.getRejectionReason())
                .build();
    }

    public Page<AdvanceResponseDto> getAdvancesForFactoryByStatus(Long factoryId, String status, Integer month, Integer year, String search, Pageable pageable) {
        Specification<com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance> spec = AdvanceSpecification.filterAdvances(factoryId, status, month, year, search);
        Page<com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance> page = advanceRepository.findAll(spec, pageable);
        return page.map(this::toResponseDto);
    }
}
