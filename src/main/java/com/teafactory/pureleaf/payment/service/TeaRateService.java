package com.teafactory.pureleaf.payment.service;


import com.teafactory.pureleaf.payment.dto.TeaRateRequestDTO;
import com.teafactory.pureleaf.payment.entity.TeaRate;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.payment.repository.TeaRateRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
