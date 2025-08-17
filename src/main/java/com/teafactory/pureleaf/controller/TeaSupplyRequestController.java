package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.TeaSupplyRequestDTO;
import com.teafactory.pureleaf.service.TeaSupplyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tea-supply-requests")
public class TeaSupplyRequestController {
    @Autowired
    private TeaSupplyRequestService teaSupplyRequestService;

    @GetMapping
    public ResponseEntity<List<TeaSupplyRequestDTO>> getAllRequests() {
        List<TeaSupplyRequestDTO> requests = teaSupplyRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<List<TeaSupplyRequestDTO>> getRequestsBySupplierId(@PathVariable Long supplierId) {
        List<TeaSupplyRequestDTO> requests = teaSupplyRequestService.getRequestsBySupplierId(supplierId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping
    public ResponseEntity<TeaSupplyRequestDTO> createTeaSupplyRequest(@RequestBody TeaSupplyRequestDTO dto) {
        // Ignore status from request, always set to pending in service
        dto.setStatus(null);
        dto.setSupplyDate(java.time.LocalDate.now()); // Set request sending date
        TeaSupplyRequestDTO created = teaSupplyRequestService.createTeaSupplyRequest(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{requestId}/bag-count")
    public ResponseEntity<TeaSupplyRequestDTO> updateEstimatedBagCount(@PathVariable Long requestId, @RequestBody TeaSupplyRequestDTO dto) {
        TeaSupplyRequestDTO updated = teaSupplyRequestService.updateEstimatedBagCount(requestId, dto.getEstimatedBagCount());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteTeaSupplyRequest(@PathVariable Long requestId) {
        teaSupplyRequestService.deleteTeaSupplyRequest(requestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/details/{requestId}")
    public ResponseEntity<TeaSupplyRequestDTO> getTeaSupplyRequestById(@PathVariable Long requestId) {
        TeaSupplyRequestDTO dto = teaSupplyRequestService.getTeaSupplyRequestById(requestId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
