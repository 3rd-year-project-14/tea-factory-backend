package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.TeaSupplyRequestDTO;
import com.teafactory.pureleaf.entity.TeaSupplyRequest;
import com.teafactory.pureleaf.repository.TeaSupplyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeaSupplyRequestService {
    @Autowired
    private TeaSupplyRequestRepository teaSupplyRequestRepository;

    public List<TeaSupplyRequestDTO> getAllRequests() {
        return teaSupplyRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TeaSupplyRequestDTO> getRequestsBySupplierId(Long supplierId) {
        return teaSupplyRequestRepository.findBySupplier_SupplierId(supplierId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TeaSupplyRequestDTO createTeaSupplyRequest(TeaSupplyRequestDTO dto) {
        TeaSupplyRequest request = new TeaSupplyRequest();
        request.setSupplyDate(java.time.LocalDate.now());
        request.setEstimatedBagCount(dto.getEstimatedBagCount());
        // Set status to 'pending' by default if not provided
        if (dto.getStatus() == null || dto.getStatus().isEmpty()) {
            request.setStatus("pending");
        } else {
            request.setStatus(dto.getStatus());
        }
        com.teafactory.pureleaf.entity.Supplier supplier = new com.teafactory.pureleaf.entity.Supplier();
        supplier.setSupplierId(dto.getSupplierId());
        request.setSupplier(supplier);
        TeaSupplyRequest saved = teaSupplyRequestRepository.save(request);
        return convertToDTO(saved);
    }

    public TeaSupplyRequestDTO updateEstimatedBagCount(Long requestId, Integer estimatedBagCount) {
        TeaSupplyRequest request = teaSupplyRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("TeaSupplyRequest not found"));
        request.setEstimatedBagCount(estimatedBagCount);
        TeaSupplyRequest saved = teaSupplyRequestRepository.save(request);
        return convertToDTO(saved);
    }

    public void deleteTeaSupplyRequest(Long requestId) {
        teaSupplyRequestRepository.deleteById(requestId);
    }

    public TeaSupplyRequestDTO getTeaSupplyRequestById(Long requestId) {
        return teaSupplyRequestRepository.findById(requestId)
            .map(this::convertToDTO)
            .orElse(null);
    }

    private TeaSupplyRequestDTO convertToDTO(TeaSupplyRequest request) {
        return new TeaSupplyRequestDTO(
                request.getRequestId(),
                request.getSupplier().getSupplierId(),
                request.getSupplyDate(),
                request.getEstimatedBagCount(),
                request.getStatus()
        );
    }
}
