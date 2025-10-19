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
        User supplier = userRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        SupplierFertilizerRequest request = SupplierFertilizerRequest.builder()
                .supplier(supplier)
                .requestDate(dto.getRequestDate())
                .note(dto.getNote())
                .status("PENDING")
                .build();
        request = requestRepository.save(request);
        List<SupplierFertilizerRequestItem> items = dto.getItems().stream().map(itemDto -> {
            FertilizerStock stock = fertilizerStockRepository.findById(itemDto.getFertilizerStockId())
                    .orElseThrow(() -> new RuntimeException("Fertilizer stock not found"));
            return SupplierFertilizerRequestItem.builder()
                    .supplierFertilizerRequest(request)
                    .fertilizerStock(stock)
                    .quantity(itemDto.getQuantity())
                    .status("PENDING")
                    .build();
        }).collect(Collectors.toList());
        itemRepository.saveAll(items);
        request.setItems(items);
        return toResponseDTO(request);
    }

    @Transactional(readOnly = true)
    public List<SupplierFertilizerRequestResponseDTO> getAllRequests() {
        List<SupplierFertilizerRequest> requests = requestRepository.findAll();
        return requests.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private SupplierFertilizerRequestResponseDTO toResponseDTO(SupplierFertilizerRequest request) {
        SupplierFertilizerRequestResponseDTO dto = new SupplierFertilizerRequestResponseDTO();
        dto.setId(request.getId());
        dto.setSupplierId(request.getSupplier().getId());
        dto.setRequestDate(request.getRequestDate());
        dto.setNote(request.getNote());
        dto.setStatus(request.getStatus());
        List<SupplierFertilizerRequestItemResponseDTO> itemDTOs = request.getItems() != null ?
            request.getItems().stream().map(item -> {
                SupplierFertilizerRequestItemResponseDTO itemDTO = new SupplierFertilizerRequestItemResponseDTO();
                itemDTO.setId(item.getId());
                itemDTO.setFertilizerStockId(item.getFertilizerStock().getId());
                itemDTO.setFertilizerStockName(item.getFertilizerStock().getCategory().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setStatus(item.getStatus());
                itemDTO.setRejectReason(item.getRejectReason());
                return itemDTO;
            }).collect(Collectors.toList()) : List.of();
        dto.setItems(itemDTOs);
        return dto;
    }
}

