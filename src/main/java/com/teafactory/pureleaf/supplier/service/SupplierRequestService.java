package com.teafactory.pureleaf.supplier.service;

import com.google.firebase.cloud.StorageClient;
import com.teafactory.pureleaf.auth.entity.Role;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import com.teafactory.pureleaf.supplier.dto.*;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import com.teafactory.pureleaf.supplier.repository.SupplierRequestRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Long createSupplierRequest(@Valid CreateSupplierRequestDTO requestDTO) {

        // 1. Find and validate user
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + requestDTO.getUserId()));

        if (!user.getRole().equals(Role.PENDING_USER)){
            throw new IllegalArgumentException("User must have role PENDING_USER to create a supplier request");
        }

        // 2. Find and validate factoryId provided
        Factory factory = factoryRepository.findById(requestDTO.getFactoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Factory not found: " + requestDTO.getFactoryId()));

        // 3. Build SupplierRequest entity
        SupplierRequest supplierRequest = new SupplierRequest();
        supplierRequest.setUser(user);
        supplierRequest.setLandSize(requestDTO.getLandSize());
        supplierRequest.setMonthlySupply(requestDTO.getMonthlySupply());
        supplierRequest.setNicImage(requestDTO.getNicImage()); // initially can be null, will be updated after image upload
        supplierRequest.setPickupLocation(requestDTO.getPickupLocation());
        supplierRequest.setLandLocation(requestDTO.getLandLocation());
        supplierRequest.setFactory(factory);

        SupplierRequest savedRequest =  supplierRequestRepository.save(supplierRequest);

        return savedRequest.getId();
    }

    // Save NIC image to Firebase and update SupplierRequest with image path
    @Transactional
    public void saveNicImage(Long supplierRequestId, MultipartFile file) throws IOException {
        SupplierRequest supplierRequest = supplierRequestRepository.findById(supplierRequestId)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));

        String fileName = "nic_" + supplierRequestId + "_" + System.currentTimeMillis() + "_" +
                StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        StorageClient.getInstance()
                .bucket()
                .create(fileName, file.getInputStream(), file.getContentType());

        supplierRequest.setNicImage(fileName);
        supplierRequestRepository.save(supplierRequest);
    }

    private SupplierRequestDTO convertToDTO(SupplierRequest supplierRequest) {
        Long factoryId = supplierRequest.getFactory() != null ? supplierRequest.getFactory().getFactoryId() : null;
        return new SupplierRequestDTO(
                supplierRequest.getId(),
                supplierRequest.getUser().getId(),
                supplierRequest.getStatus(),
                supplierRequest.getLandSize(),
                supplierRequest.getMonthlySupply(),
                supplierRequest.getRequestedRoute(),
                supplierRequest.getNicImage(),
                supplierRequest.getRejectReason(),
                supplierRequest.getPickupLocation(),
                supplierRequest.getLandLocation(),
                supplierRequest.getRejectedDate(),
                factoryId
        );
    }

    public SupplierRequestDTO updateSupplierRequest(Long id, SupplierRequestDTO requestDTO) {
        SupplierRequest supplierRequest = supplierRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));

        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            supplierRequest.setUser(user);
        }
        if (requestDTO.getStatus() != null) supplierRequest.setStatus(requestDTO.getStatus());
        if (requestDTO.getLandSize() != null) supplierRequest.setLandSize(requestDTO.getLandSize());
        if (requestDTO.getMonthlySupply() != null) supplierRequest.setMonthlySupply(requestDTO.getMonthlySupply());
        if (requestDTO.getRequestedRoute() != null) supplierRequest.setRequestedRoute(requestDTO.getRequestedRoute());
        if (requestDTO.getNicImage() != null) supplierRequest.setNicImage(requestDTO.getNicImage());
        if (requestDTO.getRejectReason() != null) supplierRequest.setRejectReason(requestDTO.getRejectReason());
        if (requestDTO.getPickupLocation() != null) supplierRequest.setPickupLocation(requestDTO.getPickupLocation());
        if (requestDTO.getLandLocation() != null) supplierRequest.setLandLocation(requestDTO.getLandLocation());

        supplierRequestRepository.save(supplierRequest);
        return convertToDTO(supplierRequest);
    }

    public SupplierRequest getSupplierRequestById(Long id) {
        return supplierRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));
    }

    public List<SupplierRequest> getAllSupplierRequests() {
        return supplierRequestRepository.findAll();
    }

    public SupplierRequest rejectSupplierRequest(Long id, String rejectReason) {
        SupplierRequest supplierRequest = supplierRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));
        supplierRequest.setStatus("rejected");
        supplierRequest.setRejectReason(rejectReason);
        supplierRequest.setRejectedDate(LocalDate.now());
        return supplierRequestRepository.save(supplierRequest);
    }

    // ================= NIC Image Handling =================

    /**
     * Upload NIC image to Firebase (private) and update SupplierRequest with objectName
     */



    // ================= Queries =================

    public List<SupplierRequest> getSupplierRequestsByUserId(Long userId) {
        return supplierRequestRepository.findByUser_Id(userId);
    }

    public List<RequestSuppliersDTO> getRequestsByFactoryIdAndStatus(Long factoryId, String status) {
        Factory factory = factoryRepository.findById(factoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Factory not found with id: " + factoryId));
        return supplierRequestRepository.findRequestsByFactoryIdAndStatus(factoryId, status);
    }

    public SupplierRequestDetailsDTO getSupplierRequestDetails(Long supplierId) {
        if (!supplierRequestRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + supplierId);
        }
        return supplierRequestRepository.findRequestDetailsById(supplierId);
    }

    public SupplierRequestStatusDTO getSupplierRequestStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!(user.getRole() == Role.PENDING_USER || user.getRole() == Role.SUPPLIER)) {
            throw new IllegalArgumentException("User does not have supplier or pending supplier role");
        }

        return supplierRequestRepository.findRequestStatusByUserId(userId);
    }
}
