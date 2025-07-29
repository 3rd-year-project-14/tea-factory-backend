package com.teafactory.pureleaf.payment.controller;


import com.teafactory.pureleaf.payment.dto.TeaRateRequestDTO;
import com.teafactory.pureleaf.payment.service.TeaRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
