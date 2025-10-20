package com.teafactory.pureleaf.fertilizer.service;

import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.fertilizer.dto.*;
import com.teafactory.pureleaf.fertilizer.entity.*;
import com.teafactory.pureleaf.fertilizer.repository.*;
import com.teafactory.pureleaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierFertilizerRequestService {
    private final SupplierFertilizerRequestRepository requestRepository;
    private final SupplierFertilizerRequestItemRepository itemRepository;
    private final FertilizerStockRepository fertilizerStockRepository;
    private final UserRepository userRepository;

    @Transactional
    public SupplierFertilizerRequestResponseDTO createRequest(SupplierFertilizerRequestCreateDTO dto) {
        System.out.println("DEBUG: Creating request for supplierId: " + dto.getSupplierId());
        User supplier = userRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        System.out.println("DEBUG: Found supplier with ID: " + supplier.getId());
        SupplierFertilizerRequest request = SupplierFertilizerRequest.builder()
                .supplier(supplier)
                .requestDate(dto.getRequestDate())
                .note(dto.getNote())
                .status("PENDING")
                .build();
        final SupplierFertilizerRequest savedRequest = requestRepository.save(request);
        List<SupplierFertilizerRequestItem> items = dto.getItems().stream().map(itemDto -> {
            FertilizerStock stock = fertilizerStockRepository.findById(itemDto.getFertilizerStockId())
                    .orElseThrow(() -> new RuntimeException("Fertilizer stock not found"));
            return SupplierFertilizerRequestItem.builder()
                    .supplierFertilizerRequest(savedRequest)
                    .fertilizerStock(stock)
                    .quantity(itemDto.getQuantity())
                    .status("PENDING")
                    .build();
        }).collect(Collectors.toList());
        itemRepository.saveAll(items);
        savedRequest.setItems(items);
        return toResponseDTO(savedRequest);
    }

    @Transactional(readOnly = true)
    public List<SupplierFertilizerRequestResponseDTO> getAllRequests() {
        List<SupplierFertilizerRequest> requests = requestRepository.findAll();
        return requests.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SupplierFertilizerRequestResponseDTO> getRequestsBySupplierId(Long supplierId) {
        List<SupplierFertilizerRequest> requests = requestRepository.findBySupplier_Id(supplierId);
        return requests.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public SupplierFertilizerRequestResponseDTO updateRequest(Long id, SupplierFertilizerRequestUpdateDTO dto) {
        SupplierFertilizerRequest request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier fertilizer request not found"));
        if (dto.getQuantity() != null && request.getItems() != null && !request.getItems().isEmpty()) {
            // Update quantity for all items (or customize as needed)
            request.getItems().forEach(item -> item.setQuantity(dto.getQuantity()));
            itemRepository.saveAll(request.getItems());
        }
        if (dto.getNote() != null) {
            request.setNote(dto.getNote());
        }
        if (dto.getStatus() != null) {
            request.setStatus(dto.getStatus());
        }
        SupplierFertilizerRequest updated = requestRepository.save(request);
        return toResponseDTO(updated);
    }

    @Transactional
    public void deleteRequest(Long id) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Supplier fertilizer request not found");
        }
        // Delete items first if necessary
        itemRepository.deleteBySupplierFertilizerRequest_Id(id);
        requestRepository.deleteById(id);
    }

    private SupplierFertilizerRequestResponseDTO toResponseDTO(SupplierFertilizerRequest request) {
        SupplierFertilizerRequestResponseDTO dto = new SupplierFertilizerRequestResponseDTO();
        dto.setId(request.getId());
        dto.setSupplierId(request.getSupplier().getId());
        dto.setSupplierName(request.getSupplier().getName());
        dto.setRequestDate(request.getRequestDate());
        dto.setNote(request.getNote());
        dto.setStatus(request.getStatus());
        List<SupplierFertilizerRequestItemResponseDTO> itemDTOs = request.getItems() != null ?
            request.getItems().stream().map(item -> {
                SupplierFertilizerRequestItemResponseDTO itemDTO = new SupplierFertilizerRequestItemResponseDTO();
                itemDTO.setId(item.getId());
                itemDTO.setFertilizerStockId(item.getFertilizerStock().getId());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setStatus(item.getStatus());
                itemDTO.setRejectReason(item.getRejectReason());
                itemDTO.setProductName(
                    item.getFertilizerStock().getCompany().getName() + " " +
                    item.getFertilizerStock().getCategory().getName()
                );
                return itemDTO;
            }).collect(Collectors.toList()) : List.of();
        dto.setItems(itemDTOs);
        return dto;
    }
}
