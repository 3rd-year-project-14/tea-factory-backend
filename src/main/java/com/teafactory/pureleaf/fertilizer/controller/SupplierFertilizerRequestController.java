package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.SupplierFertilizerRequestCreateDTO;
import com.teafactory.pureleaf.fertilizer.dto.SupplierFertilizerRequestResponseDTO;
import com.teafactory.pureleaf.fertilizer.service.SupplierFertilizerRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-fertilizer-requests")
@RequiredArgsConstructor
public class SupplierFertilizerRequestController {
    private final SupplierFertilizerRequestService requestService;

    @PostMapping
    public ResponseEntity<SupplierFertilizerRequestResponseDTO> createRequest(@RequestBody SupplierFertilizerRequestCreateDTO dto) {
        SupplierFertilizerRequestResponseDTO response = requestService.createRequest(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SupplierFertilizerRequestResponseDTO>> getAllRequests() {
        List<SupplierFertilizerRequestResponseDTO> list = requestService.getAllRequests();
        return ResponseEntity.ok(list);
    }
}

