package com.teafactory.pureleaf.fertilizer.service;

import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.fertilizer.dto.*;
import com.teafactory.pureleaf.fertilizer.entity.*;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCompanyRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerRequestRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerRequestItemRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerStockRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FertilizerRequestService {

    private final FertilizerRequestRepository requestRepository;
    private final FertilizerCategoryRepository categoryRepository;
    private final FertilizerCompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final FertilizerStockRepository fertilizerStockRepository;
    private final FertilizerRequestItemRepository itemRepository;

    @Transactional
    public FertilizerRequestDTO createRequest(CreateFertilizerRequestDTO dto) {
        log.info("Creating fertilizer request for fertilizerStockId: {}", dto.getFertilizerStockId());

        // Fetch FertilizerStock entity
        FertilizerStock fertilizerStock = fertilizerStockRepository.findById(dto.getFertilizerStockId())
                .orElseThrow(() -> new ResourceNotFoundException("Fertilizer stock not found: " + dto.getFertilizerStockId()));

        // Use related entities from FertilizerStock
        FertilizerCategory category = fertilizerStock.getCategory();
        FertilizerCompany company = fertilizerStock.getCompany();
        User user = fertilizerStock.getUser();

        // Create and save the request
        FertilizerRequest request = FertilizerRequest.builder()
                .category(category)
                .company(company)
                .user(user)
                .quantity(dto.getQuantity())
                .status(FertilizerRequestStatus.PENDING)
                .build();
        request = requestRepository.save(request);

        log.info("Successfully created fertilizer request with ID: {}", request.getId());

        return toDTO(request);
    }

    @Transactional
    public List<FertilizerRequestDTO> createBatchRequests(BatchFertilizerRequestDTO batchDto) {
        log.info("Creating batch of {} fertilizer requests", batchDto.getRequests().size());

        return batchDto.getRequests().stream()
                .map(this::createRequest)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FertilizerRequestDTO> getAllRequests(String status) {
        log.info("Fetching all fertilizer requests with status filter: {}", status);

        if (status != null && !status.isBlank()) {
            try {
                FertilizerRequestStatus requestStatus = FertilizerRequestStatus.valueOf(status.toUpperCase());
                return requestRepository.findByStatusWithDetails(requestStatus)
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        }

        return requestRepository.findAllWithDetails()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FertilizerRequestDTO getRequestById(Long id) {
        log.info("Fetching fertilizer request by ID: {}", id);

        FertilizerRequest request = requestRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fertilizer request not found: " + id));

        return toDTO(request);
    }

    @Transactional(readOnly = true)
    public List<FertilizerRequestDTO> getRequestsByUser(Long userId) {
        log.info("Fetching fertilizer requests for user: {}", userId);

        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        return requestRepository.findByUser_Id(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FertilizerRequestDTO> getRequestsByUserAndStatus(Long userId, String status) {
        log.info("Fetching fertilizer requests for user: {} with status: {}", userId, status);

        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        try {
            FertilizerRequestStatus requestStatus = FertilizerRequestStatus.valueOf(status.toUpperCase());
            return requestRepository.findByUser_IdAndStatus(userId, requestStatus)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public FertilizerRequestDTO updateRequestStatus(Long id, UpdateFertilizerRequestStatusDTO dto) {
        log.info("Updating fertilizer request {} status to: {}", id, dto.getStatus());

        FertilizerRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fertilizer request not found: " + id));

        // Validate rejection reason
        if (dto.getStatus() == FertilizerRequestStatus.REJECTED) {
            if (dto.getRejectReason() == null || dto.getRejectReason().trim().isEmpty()) {
                throw new IllegalArgumentException("Reject reason is required when status is REJECTED");
            }
        } else {
            // Clear reject reason for non-rejected statuses
            dto.setRejectReason(null);
        }

        // Update status and reason
        request.setStatus(dto.getStatus());
        request.setRejectReason(dto.getRejectReason());

        FertilizerRequest updatedRequest = requestRepository.save(request);
        log.info("Successfully updated fertilizer request {} status to: {}", id, dto.getStatus());

        return toDTO(updatedRequest);
    }

    @Transactional
    public BatchFertilizerRequestResponseDTO createBatchRequestSingleParent(BatchFertilizerRequestDTO batchDto) {
        if (batchDto.getRequests().isEmpty()) {
            throw new IllegalArgumentException("Batch request must contain at least one item.");
        }

        // Use the first item's fertilizerStockId to get category, company, user
        CreateFertilizerRequestDTO first = batchDto.getRequests().get(0);
        FertilizerStock fertilizerStock = fertilizerStockRepository.findById(first.getFertilizerStockId())
                .orElseThrow(() -> new ResourceNotFoundException("Fertilizer stock not found: " + first.getFertilizerStockId()));
        FertilizerCategory category = fertilizerStock.getCategory();
        FertilizerCompany company = fertilizerStock.getCompany();
        User user = fertilizerStock.getUser();

        // Calculate total quantity for parent request
        int totalQuantity = batchDto.getRequests().stream()
            .mapToInt(CreateFertilizerRequestDTO::getQuantity)
            .sum();
        // Create parent request
        FertilizerRequest parentRequest = FertilizerRequest.builder()
                .category(category)
                .company(company)
                .user(user)
                .quantity(totalQuantity)
                .status(FertilizerRequestStatus.PENDING)
                .requestDate(batchDto.getRequestDate())
                .build();
        parentRequest = requestRepository.save(parentRequest);

        final FertilizerRequest finalParentRequest = parentRequest;
        // Create items
        List<FertilizerRequestItemDTO> itemDTOs = batchDto.getRequests().stream().map(itemDto -> {
            FertilizerRequestItem item = new FertilizerRequestItem();
            item.setFertilizerRequest(finalParentRequest);
            item.setFertilizerStockId(itemDto.getFertilizerStockId());
            item.setQuantity(itemDto.getQuantity());
            item = itemRepository.save(item);
            // Fetch FertilizerStock for product name and weight
            FertilizerStock stock = fertilizerStockRepository.findById(itemDto.getFertilizerStockId())
                .orElse(null);
            String productName = (stock != null && stock.getCompany() != null && stock.getCategory() != null)
                ? stock.getCompany().getName() + " " + stock.getCategory().getName()
                : null;
            Double weightPerQuantity = (stock != null) ? stock.getWeightPerQuantity() : null;
            Long supplierId = (stock != null && stock.getUser() != null) ? stock.getUser().getId() : null;
            String supplierName = (stock != null && stock.getUser() != null) ? stock.getUser().getName() : null;
            return FertilizerRequestItemDTO.builder()
                    .id(item.getId())
                    .fertilizerStockId(item.getFertilizerStockId())
                    .quantity(item.getQuantity())
                    .productName(productName)
                    .weightPerQuantity(weightPerQuantity)
                    .supplierId(supplierId)
                    .supplierName(supplierName)
                    .build();
        }).collect(java.util.stream.Collectors.toList());

        return BatchFertilizerRequestResponseDTO.builder()
                .requestId(parentRequest.getId())
                .items(itemDTOs)
                .build();
    }

    @Transactional(readOnly = true)
    public BatchFertilizerRequestResponseDTO getBatchRequestById(Long parentRequestId) {
        FertilizerRequest parentRequest = requestRepository.findById(parentRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Fertilizer request not found: " + parentRequestId));
        List<FertilizerRequestItem> items = itemRepository.findByFertilizerRequest_Id(parentRequestId);
        List<FertilizerRequestItemDTO> itemDTOs = items.stream().map(item -> {
            FertilizerStock stock = fertilizerStockRepository.findById(item.getFertilizerStockId()).orElse(null);
            String productName = (stock != null && stock.getCompany() != null && stock.getCategory() != null)
                    ? stock.getCompany().getName() + " " + stock.getCategory().getName() : null;
            Double weightPerQuantity = (stock != null) ? stock.getWeightPerQuantity() : null;
            Long supplierId = (stock != null && stock.getUser() != null) ? stock.getUser().getId() : null;
            String supplierName = (stock != null && stock.getUser() != null) ? stock.getUser().getName() : null;
            return FertilizerRequestItemDTO.builder()
                    .id(item.getId())
                    .fertilizerStockId(item.getFertilizerStockId())
                    .quantity(item.getQuantity())
                    .productName(productName)
                    .weightPerQuantity(weightPerQuantity)
                    .supplierId(supplierId)
                    .supplierName(supplierName)
                    .status(item.getStatus())
                    .rejectReason(item.getRejectReason())
                    .build();
        }).collect(java.util.stream.Collectors.toList());
        return BatchFertilizerRequestResponseDTO.builder()
                .requestId(parentRequest.getId())
                .items(itemDTOs)
                .build();
    }

    @Transactional(readOnly = true)
    public List<BatchFertilizerRequestResponseDTO> getAllBatchRequests() {
        List<FertilizerRequest> parentRequests = requestRepository.findAll();
        return parentRequests.stream()
            .map(parentRequest -> {
                List<FertilizerRequestItem> items = itemRepository.findByFertilizerRequest_Id(parentRequest.getId());
                List<FertilizerRequestItemDTO> itemDTOs = items.stream().map(item -> {
                    FertilizerStock stock = fertilizerStockRepository.findById(item.getFertilizerStockId()).orElse(null);
                    String productName = (stock != null && stock.getCompany() != null && stock.getCategory() != null)
                            ? stock.getCompany().getName() + " " + stock.getCategory().getName() : null;
                    Double weightPerQuantity = (stock != null) ? stock.getWeightPerQuantity() : null;
                    Long supplierId = (stock != null && stock.getUser() != null) ? stock.getUser().getId() : null;
                    String supplierName = (stock != null && stock.getUser() != null) ? stock.getUser().getName() : null;
                    return FertilizerRequestItemDTO.builder()
                            .id(item.getId())
                            .fertilizerStockId(item.getFertilizerStockId())
                            .quantity(item.getQuantity())
                            .productName(productName)
                            .weightPerQuantity(weightPerQuantity)
                            .supplierId(supplierId)
                            .supplierName(supplierName)
                            .status(item.getStatus())
                            .rejectReason(item.getRejectReason())
                            .build();
                }).collect(java.util.stream.Collectors.toList());
                return BatchFertilizerRequestResponseDTO.builder()
                        .requestId(parentRequest.getId())
                        .items(itemDTOs)
                        .build();
            })
            .filter(batch -> batch.getItems() != null && !batch.getItems().isEmpty())
            .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public FertilizerRequestDTO createFertilizerStockRequest(NewFertilizerStockRequestDTO dto) {
        log.info("Creating new fertilizer stock request: categoryId={}, companyId={}, userId={}, quantity={}", dto.getCategoryId(), dto.getCompanyId(), dto.getUserId(), dto.getQuantity());

        FertilizerCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Fertilizer category not found: " + dto.getCategoryId()));
        FertilizerCompany company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Fertilizer company not found: " + dto.getCompanyId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.getUserId()));

        FertilizerRequest request = FertilizerRequest.builder()
                .category(category)
                .company(company)
                .user(user)
                .quantity(dto.getQuantity())
                .note(dto.getNote())
                .status(FertilizerRequestStatus.PENDING)
                .build();
        request = requestRepository.save(request);
        log.info("Successfully created fertilizer stock request with ID: {}", request.getId());
        return toDTO(request);
    }

    @Transactional(readOnly = true)
    public List<FertilizerRequestDTO> getAllFertilizerStockRequests() {
        log.info("Fetching all fertilizer stock requests");
        return requestRepository.findAllWithDetails()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private FertilizerRequestDTO toDTO(FertilizerRequest request) {
        return FertilizerRequestDTO.builder()
                .id(request.getId())
                .categoryId(request.getCategory().getId())
                .categoryName(request.getCategory().getName())
                .companyId(request.getCompany().getId())
                .companyName(request.getCompany().getName())
                .userId(request.getUser().getId())
                .userName(request.getUser().getName() != null ? request.getUser().getName() : request.getUser().getEmail())
                .quantity(request.getQuantity())
                .note(request.getNote())
                .status(request.getStatus())
                .rejectReason(request.getRejectReason())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}
