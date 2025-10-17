package com.teafactory.pureleaf.advance.service;

import com.teafactory.pureleaf.advance.dto.AdvanceRequestCreateDTO;
import com.teafactory.pureleaf.advance.dto.AdvanceRequestResponseDTO;
import com.teafactory.pureleaf.advance.entity.AdvanceRequest;
import com.teafactory.pureleaf.advance.repository.AdvanceRequestRepository;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AdvanceRequestService {
    private AdvanceRequestRepository advanceRequestRepository;
    private SupplierRepository supplierRepository;

    public AdvanceRequest createAdvanceRequest(AdvanceRequestCreateDTO dto) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + dto.getSupplierId()));

        AdvanceRequest advanceRequest = new AdvanceRequest();
        advanceRequest.setSupplier(supplier);
        advanceRequest.setAmount(dto.getAmount());
        advanceRequest.setPaymentMethod(dto.getPaymentMethod());
        advanceRequest.setDate(LocalDate.now());
        advanceRequest.setStatus(AdvanceRequest.Status.Pending);
        return advanceRequestRepository.save(advanceRequest);
    }
    public List<AdvanceRequestResponseDTO> getPendingAdvanceRequest() {
        List<AdvanceRequest> pendingRequests = advanceRequestRepository.findByStatus(AdvanceRequest.Status.Pending);

        if (pendingRequests.isEmpty()) {
            throw new ResourceNotFoundException("No pending advance requests found");
        }

        return pendingRequests.stream()
                .map(this::toResponseDTO)
                .toList();
    }
    public List<AdvanceRequestResponseDTO> getApprovedAdvanceRequest() {
        List<AdvanceRequest> approvedRequests = advanceRequestRepository.findByStatus(AdvanceRequest.Status.Approved);
        if (approvedRequests.isEmpty()) {
            throw new ResourceNotFoundException("No approved advance requests found");
        }

        return approvedRequests.stream()
                .map(this::toResponseDTO)
                .toList();
    }


    private AdvanceRequestResponseDTO toResponseDTO(AdvanceRequest request) {
        return new AdvanceRequestResponseDTO(
                request.getAdvanceId(),
                request.getSupplier().getSupplierId(),
                request.getAmount(),
                request.getPaymentMethod(),
                request.getStatus(),
                request.getDate()
        );
    }


}
