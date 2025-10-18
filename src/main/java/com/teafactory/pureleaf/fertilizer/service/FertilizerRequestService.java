package com.teafactory.pureleaf.fertilizer.service;

import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.fertilizer.dto.*;
import com.teafactory.pureleaf.fertilizer.entity.*;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCompanyRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerRequestRepository;
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

    @Transactional
    public FertilizerRequestDTO createRequest(CreateFertilizerRequestDTO dto) {
        log.info("Creating fertilizer request for user: {}, category: {}, company: {}",
                 dto.getUserId(), dto.getCategoryId(), dto.getCompanyId());

        // Validate and fetch entities
        FertilizerCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.getCategoryId()));

        FertilizerCompany company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + dto.getCompanyId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.getUserId()));

        // Validate company-category relationship (if company has specific categories)
        if (company.getCategories() != null && !company.getCategories().isEmpty()) {
            boolean categoryFound = company.getCategories().stream()
                    .anyMatch(c -> c.getId().equals(category.getId()));
            if (!categoryFound) {
                throw new IllegalArgumentException("Selected company is not associated with the chosen category");
            }
        }

        // Create and save the request
        FertilizerRequest request = FertilizerRequest.builder()
                .category(category)
                .company(company)
                .user(user)
                .quantity(dto.getQuantity())
                .note(dto.getNote())
                .status(FertilizerRequestStatus.PENDING)
                .build();

        FertilizerRequest savedRequest = requestRepository.save(request);
        log.info("Successfully created fertilizer request with ID: {}", savedRequest.getId());

        return toDTO(savedRequest);
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
