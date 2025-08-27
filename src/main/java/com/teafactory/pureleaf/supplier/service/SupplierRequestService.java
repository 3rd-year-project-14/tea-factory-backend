package com.teafactory.pureleaf.supplier.service;


import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import com.teafactory.pureleaf.supplier.dto.RequestSuppliersDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierRequestDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierRequestDetailsDTO;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import com.teafactory.pureleaf.supplier.repository.SupplierRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import org.springframework.util.StringUtils;

@Service
public class SupplierRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private SupplierRequestRepository supplierRequestRepository;

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
        supplierRequestRepository.save(supplierRequest);
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
        return requestDTO;
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
    public String saveNicImage(Long supplierRequestId, MultipartFile file) throws IOException {
        SupplierRequest supplierRequest = supplierRequestRepository.findById(supplierRequestId)
                .orElseThrow(() -> new RuntimeException("SupplierRequest not found"));

        String fileName = "nic_" + supplierRequestId + "_" + System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Blob blob = StorageClient.getInstance().bucket().create(fileName, file.getInputStream(), file.getContentType());
        // Generate Firebase public download URL
        String fileUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", blob.getBucket(), java.net.URLEncoder.encode(blob.getName(), java.nio.charset.StandardCharsets.UTF_8));
        supplierRequest.setNicImage(fileUrl);
        supplierRequestRepository.save(supplierRequest);
        return fileUrl;
    }
    public String saveNicImageFile(MultipartFile file) throws IOException {
        String fileName = "nic_" + System.currentTimeMillis() + "_" + org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
        com.google.cloud.storage.Blob blob = com.google.firebase.cloud.StorageClient.getInstance().bucket().create(fileName, file.getInputStream(), file.getContentType());
        return String.format("https://storage.googleapis.com/%s/%s", blob.getBucket(), blob.getName());
    }
    public List<SupplierRequest> getSupplierRequestsByUserId(Long userId) {
        return supplierRequestRepository.findByUser_Id(userId);
    }

    public List<RequestSuppliersDTO> getRequestsByFactoryIdAndStatus(Long factoryId, String status) {
        Factory factory = factoryRepository.findById(factoryId).orElse(null);
        if (factory == null) {
            throw new ResourceNotFoundException("Factory not found with id: " + factoryId);
        }
        List<RequestSuppliersDTO> requests = supplierRequestRepository.findRequestsByFactoryIdAndStatus(factoryId, status);
        return requests;
    }

    public SupplierRequestDetailsDTO getSupplierRequestDetails (Long supplierId) {
        if(!supplierRequestRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + supplierId);
        }
        SupplierRequestDetailsDTO requestDetails = supplierRequestRepository.findRequestDetailsById(supplierId);
        return requestDetails;
    }

}
