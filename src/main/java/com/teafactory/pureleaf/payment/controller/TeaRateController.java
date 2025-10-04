package com.teafactory.pureleaf.payment.controller;


import com.teafactory.pureleaf.payment.dto.TeaRateRequestDTO;
import com.teafactory.pureleaf.payment.service.TeaRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.teafactory.pureleaf.payment.entity.TeaRate;
import com.teafactory.pureleaf.payment.dto.TeaRateResponseDTO;

import java.util.List;


@RestController
@RequestMapping("/api/tea_rates")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TeaRateController {

    private final TeaRateService teaRateService;

    @PostMapping
    public ResponseEntity<String> createTeaRate(@RequestBody TeaRateRequestDTO dto) {
        teaRateService.createTeaRate(dto);
        return ResponseEntity.ok("Tea rate created successfully");
    }

    @GetMapping("/pending")
    public List<TeaRateResponseDTO> getPendingTeaRates() {
        return teaRateService.getPendingTeaRateDTOs();
    }


    @GetMapping
    public List<TeaRateResponseDTO> getAllTeaRates() {
        return teaRateService.getAllTeaRateDTOs();
    }

    @GetMapping("/approved")
    public List<TeaRateResponseDTO> getApprovedTeaRates() {
        return teaRateService.getApprovedTeaRateDTOs();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<TeaRateResponseDTO> approveTeaRate(@PathVariable Long id) {
        TeaRateResponseDTO updatedTeaRate = teaRateService.approveTeaRate(id);
        return ResponseEntity.ok(updatedTeaRate);
    }

    @PutMapping("/{id}/adjust")
    public ResponseEntity<TeaRateResponseDTO> adjustTeaRate(
            @PathVariable Long id,
            @RequestBody TeaRateRequestDTO dto) {
        TeaRateResponseDTO updatedTeaRate = teaRateService.adjustTeaRate(id, dto.getAdjustedRate(), dto.getAdjustmentReason());
        return ResponseEntity.ok(updatedTeaRate);
    }

}
