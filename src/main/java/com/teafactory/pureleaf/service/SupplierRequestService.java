package com.teafactory.pureleaf.service;


import com.teafactory.pureleaf.dto.SupplierRequestDTO;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.SupplierRequest;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.repository.SupplierRequestRepo;
import com.teafactory.pureleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import org.springframework.util.StringUtils;

@Service
public class SupplierRequestService {
    @Autowired
    private SupplierRequestRepo supplierRequestRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FactoryRepository factoryRepository;

    // Fetch the User entity from DB


    public SupplierRequestDTO createSupplierRequest(SupplierRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SupplierRequest supplierRequest = new SupplierRequest();

        supplierRequest.setUser(user);
        supplierRequest.setStatus(requestDTO.getStatus());
        supplierRequest.setLandSize(requestDTO.getLandSize());
        supplierRequest.setMonthlySupply(requestDTO.getMonthlySupply());
        supplierRequest.setRequestedRoute(requestDTO.getRequestedRoute());
        supplierRequest.setNicImage(requestDTO.getNicImage());
        supplierRequest.setRejectReason(requestDTO.getRejectReason());
        supplierRequest.setPickupLocation(requestDTO.getPickupLocation());
        supplierRequest.setLandLocation(requestDTO.getLandLocation());
        if (requestDTO.getFactoryId() != null) {
            Factory factory = factoryRepository.findById(requestDTO.getFactoryId())
                .orElseThrow(() -> new RuntimeException("Factory not found"));
            supplierRequest.setFactory(factory);
        }
        supplierRequestRepo.save(supplierRequest);
        return convertToDTO(supplierRequest);
    }

    private SupplierRequestDTO convertToDTO(SupplierRequest supplierRequest) {
        Long factoryId = supplierRequest.getFactory() != null ? supplierRequest.getFactory().getFactoryId() : null;
        return new SupplierRequestDTO(
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
        SupplierRequest supplierRequest = supplierRequestRepo.findById(id)
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

        supplierRequestRepo.save(supplierRequest);
        return requestDTO;
    }

    public SupplierRequest getSupplierRequestById(Long id) {
        return supplierRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));
    }

    public List<SupplierRequest> getAllSupplierRequests() {
        return supplierRequestRepo.findAll();
    }
    public SupplierRequest rejectSupplierRequest(Long id, String rejectReason) {
        SupplierRequest supplierRequest = supplierRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));
        supplierRequest.setStatus("rejected");
        supplierRequest.setRejectReason(rejectReason);
        supplierRequest.setRejectedDate(LocalDateTime.now());
        return supplierRequestRepo.save(supplierRequest);
    }
    public String saveNicImage(Long supplierRequestId, MultipartFile file) throws IOException {
        SupplierRequest supplierRequest = supplierRequestRepo.findById(supplierRequestId)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));

        String fileName = "nic_" + supplierRequestId + "_" + System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Blob blob = StorageClient.getInstance().bucket().create(fileName, file.getInputStream(), file.getContentType());
        // Generate Firebase public download URL
        String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", blob.getBucket(), java.net.URLEncoder.encode(blob.getName(), java.nio.charset.StandardCharsets.UTF_8));
        supplierRequest.setNicImage(fileUrl);
        supplierRequestRepo.save(supplierRequest);
        return fileUrl;
    }
    public String saveNicImageFile(MultipartFile file) throws IOException {
        String fileName = "nic_" + System.currentTimeMillis() + "_" + org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
        com.google.cloud.storage.Blob blob = com.google.firebase.cloud.StorageClient.getInstance().bucket().create(fileName, file.getInputStream(), file.getContentType());
        return String.format("https://storage.googleapis.com/%s/%s", blob.getBucket(), blob.getName());
    }
    public List<SupplierRequest> getSupplierRequestsByUserId(Long userId) {
        return supplierRequestRepo.findByUser_Id(userId);
    }
    // This service currently does not have any methods, but you can add methods to handle supplier requests as needed.
}
