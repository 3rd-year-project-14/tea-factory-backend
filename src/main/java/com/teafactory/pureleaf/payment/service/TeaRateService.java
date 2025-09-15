package com.teafactory.pureleaf.payment.service;


import com.teafactory.pureleaf.payment.dto.TeaRateRequestDTO;
import com.teafactory.pureleaf.payment.dto.TeaRateResponseDTO;
import com.teafactory.pureleaf.payment.entity.TeaRate;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.payment.repository.TeaRateRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeaRateService {

    private final TeaRateRepository teaRateRepository;
    private final UserRepository userRepository;

    public void createTeaRate(TeaRateRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TeaRate teaRate = TeaRate.builder()
                .user(user)
                .month(dto.getMonth())
                .nsa(dto.getNsa())
                .gsa(dto.getGsa())
                .monthlyRate(dto.getMonthlyRate())
                .totalWeight(dto.getTotalWeight())
                .finalRatePerKg(dto.getFinalRatePerKg())
                .totalPayout(dto.getTotalPayout())
                .status(TeaRate.Status.PENDING)
                .calculatedDate(LocalDateTime.now())
                .build();

        teaRateRepository.save(teaRate);
    }

    public List<TeaRate> getAllTeaRates() {
        return teaRateRepository.findAll();
    }

    public List<TeaRateResponseDTO> getAllTeaRateDTOs() {
        return teaRateRepository.findAll().stream()
            .map(rate -> toResponseDTO(rate))
            .toList();
    }

    public TeaRateResponseDTO approveTeaRate(Long id) {
        TeaRate teaRate = teaRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tea rate not found"));
        teaRate.setStatus(TeaRate.Status.APPROVED);
        // If adjustedRate is null, set it to monthlyRate
        if (teaRate.getAdjustedRate() == null) {
            teaRate.setAdjustedRate(teaRate.getMonthlyRate());
        }
        teaRateRepository.save(teaRate);
        return toResponseDTO(teaRate);
    }

    public TeaRateResponseDTO adjustTeaRate(Long id, java.math.BigDecimal adjustedRate, String adjustmentReason) {
        TeaRate teaRate = teaRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tea rate not found"));
        teaRate.setAdjustedRate(adjustedRate);
        teaRate.setAdjustmentReason(adjustmentReason);
        teaRate.setStatus(TeaRate.Status.APPROVED); // Always set to APPROVED when adjusted
        teaRateRepository.save(teaRate);
        return toResponseDTO(teaRate);
    }

    public List<TeaRateResponseDTO> getApprovedTeaRateDTOs() {
        return teaRateRepository.findAll().stream()
            .filter(rate -> rate.getStatus() == TeaRate.Status.APPROVED)
            .map(this::toResponseDTO)
            .toList();
    }

    public List<TeaRateResponseDTO> getPendingTeaRateDTOs() {
        return teaRateRepository.findAll().stream()
            .filter(rate -> rate.getStatus() == TeaRate.Status.PENDING)
            .map(this::toResponseDTO)
            .toList();
    }

    private TeaRateResponseDTO toResponseDTO(TeaRate rate) {
        String createdAt = rate.getCalculatedDate() != null ? rate.getCalculatedDate().toString() : null;
        return new TeaRateResponseDTO(
                rate.getMonth(),
                rate.getNsa(),
                rate.getGsa(),
                rate.getMonthlyRate(),
                rate.getTotalWeight(),
                rate.getFinalRatePerKg(),
                rate.getTotalPayout(),
                createdAt,
                rate.getUser() != null && rate.getUser().getFactory() != null ? rate.getUser().getFactory().getFactoryId() : null,
                rate.getUser() != null && rate.getUser().getFactory() != null ? rate.getUser().getFactory().getName() : null,
                rate.getUser() != null ? rate.getUser().getName() : null,
                rate.getStatus() != null ? rate.getStatus().name() : null,
                rate.getAdjustedRate(),
                rate.getAdjustmentReason(),
                rate.getTeaRateId()
        );
    }
}
