package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.SupplierFertilizerRequestCreateDTO;
import com.teafactory.pureleaf.fertilizer.dto.SupplierFertilizerRequestResponseDTO;
import com.teafactory.pureleaf.fertilizer.dto.SupplierFertilizerRequestUpdateDTO;
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

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierFertilizerRequestResponseDTO>> getRequestsBySupplier(@PathVariable Long supplierId) {
        List<SupplierFertilizerRequestResponseDTO> list = requestService.getRequestsBySupplierId(supplierId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierFertilizerRequestResponseDTO> updateRequest(@PathVariable Long id, @RequestBody SupplierFertilizerRequestUpdateDTO dto) {
        SupplierFertilizerRequestResponseDTO response = requestService.updateRequest(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
