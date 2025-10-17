package com.teafactory.pureleaf.advance.controller;

import com.teafactory.pureleaf.advance.dto.AdvanceRequestCreateDTO;
import com.teafactory.pureleaf.advance.dto.AdvanceRequestResponseDTO;
import com.teafactory.pureleaf.advance.entity.AdvanceRequest;
import com.teafactory.pureleaf.advance.service.AdvanceRequestService;
import com.teafactory.pureleaf.payment.dto.TeaRateResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/advance-requests")
@AllArgsConstructor
public class AdvanceRequestController {

    private AdvanceRequestService advanceRequestService;

    @PostMapping
    public ResponseEntity<AdvanceRequestResponseDTO> createAdvanceRequest(@RequestBody AdvanceRequestCreateDTO dto) {
        AdvanceRequest created = advanceRequestService.createAdvanceRequest(dto);

        AdvanceRequestResponseDTO response = new AdvanceRequestResponseDTO(
                created.getAdvanceId(),
                created.getSupplier().getSupplierId(),
                created.getAmount(),
                created.getPaymentMethod(),
                created.getStatus(),
                created.getDate()
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/pending")
    public List<AdvanceRequestResponseDTO> getPendingAdvanceRequest() {
        return advanceRequestService.getPendingAdvanceRequest();
    }
    @GetMapping("/approved")
    public List<AdvanceRequestResponseDTO> getApprovedAdvanceRequest() {
        return advanceRequestService.getApprovedAdvanceRequest();
    }

}