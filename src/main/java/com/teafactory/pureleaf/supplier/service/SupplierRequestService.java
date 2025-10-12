package com.teafactory.pureleaf.supplier.service;

import com.google.firebase.cloud.StorageClient;
import com.teafactory.pureleaf.auth.entity.Role;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import com.teafactory.pureleaf.supplier.dto.*;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import com.teafactory.pureleaf.supplier.repository.SupplierRequestRepository;
import com.teafactory.pureleaf.supplier.specs.SupplierRequestSpecs;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class SupplierRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private SupplierRequestRepository supplierRequestRepository;

    // Create a new supplier request
    @Transactional
    @PreAuthorize("hasRole('PENDING_USER')")
    public Long createSupplierRequest(@Valid CreateSupplierRequestDTO requestDTO) {
        // 1. Find and validate user
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + requestDTO.getUserId()));

        // Check if a supplier request already exists for this user
        if (!supplierRequestRepository.findByUser_Id(user.getId()).isEmpty()) {
            throw new IllegalStateException("A supplier request already exists for this user.");
        }

        // 2. Find and validate factoryId provided
        Factory factory = factoryRepository.findById(requestDTO.getFactoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Factory not found: " + requestDTO.getFactoryId()));

        // 3. Build SupplierRequest entity
        SupplierRequest supplierRequest = SupplierRequest.builder()
        .user(user)
        .landSize(requestDTO.getLandSize())
        .monthlySupply(requestDTO.getMonthlySupply())
        .pickupLocation(requestDTO.getPickupLocation())
        .landLocation(requestDTO.getLandLocation())
        .factory(factory)
        .status("pending")
        .requestedDate(LocalDate.now())
        .build();

        SupplierRequest savedRequest =  supplierRequestRepository.save(supplierRequest);

        return savedRequest.getId();

    }

    // Save NIC image to Firebase and update SupplierRequest with image path
    @Transactional
    public void saveNicImage(Long supplierRequestId, MultipartFile file) throws IOException {

        SupplierRequest supplierRequest = supplierRequestRepository.findById(supplierRequestId)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));

        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String fileName = "nic_" + supplierRequestId + "_" + timestamp + "_" +
                StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        StorageClient.getInstance()
                .bucket()
                .create(fileName, file.getInputStream(), file.getContentType());

        supplierRequest.setNicImage(fileName);
        supplierRequestRepository.save(supplierRequest);

    }

    // Retrieves supplier requests for a specific user
    public List<SupplierRequest> getSupplierRequestsByUserId(Long userId) {
        return supplierRequestRepository.findByUser_Id(userId);
    }

    // Retrieves supplier requests for a specific factory and status
    public List<RequestSuppliersDTO> getRequestsByFactoryIdAndStatus(Long factoryId, String status) {
        Factory factory = factoryRepository.findById(factoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Factory not found with id: " + factoryId));
        return supplierRequestRepository.findRequestsByFactoryIdAndStatus(factoryId, status);
    }

    // Retrieves supplier requests for a specific factory, status with pagination and search
    public Page<RequestSuppliersDTO> getRequestsByFactoryIdAndStatus(Long factoryId, String status, String search, Pageable pageable) {

        Specification<SupplierRequest> spec = SupplierRequestSpecs.hasFactory(factoryId)
                .and(SupplierRequestSpecs.hasStatus(status));

        if (search != null && !search.isEmpty()) {
            spec = spec.and(SupplierRequestSpecs.searchByNameOrId(search));
        }
        Page<SupplierRequest> page = supplierRequestRepository.findAll(spec, pageable);
        return page.map(s -> new RequestSuppliersDTO(
            s.getId(),
            s.getUser().getName(),
            s.getMonthlySupply(),
            s.getRequestedDate(),
            s.getRejectedDate()
        ));
    }

    // Gets detailed information about a supplier request by supplier ID
    public SupplierRequestDetailsDTO getSupplierRequestDetails(Long supplierId) {
        if (!supplierRequestRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + supplierId);
        }
        return supplierRequestRepository.findRequestDetailsById(supplierId);
    }

    // Returns the status of a supplier request for a given user ID
    public SupplierRequestStatusDTO getSupplierRequestStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!(user.getRole() == Role.PENDING_USER || user.getRole() == Role.SUPPLIER)) {
            throw new IllegalArgumentException("User does not have supplier or pending supplier role");
        }
        return supplierRequestRepository.findRequestStatusByUserId(userId);
    }
}
