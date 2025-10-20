package com.teafactory.pureleaf.payment.controller;

import com.teafactory.pureleaf.payment.dto.MonthlyPaymentRequestDTO;
import com.teafactory.pureleaf.payment.dto.MonthlyPaymentResponseDTO;
import com.teafactory.pureleaf.payment.entity.MonthlyPayment;
import com.teafactory.pureleaf.payment.service.MonthlyPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monthly-payments")
public class MonthlyPaymentController {
    @Autowired
    private MonthlyPaymentService monthlyPaymentService;

    @PostMapping
    public ResponseEntity<MonthlyPaymentResponseDTO> addMonthlyPayment(@RequestBody MonthlyPaymentRequestDTO dto) {
        MonthlyPayment payment = monthlyPaymentService.addMonthlyPayment(dto);
        MonthlyPaymentResponseDTO responseDTO = monthlyPaymentService.toResponseDTO(payment);
        return ResponseEntity.ok(responseDTO);
    }
}
