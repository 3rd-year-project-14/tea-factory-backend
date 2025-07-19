package com.teafactory.pureleaf.service;


import com.teafactory.pureleaf.dto.SupplierRequestDTO;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.SupplierRequest;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.repository.SupplierRequestRepo;
import com.teafactory.pureleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    // This service currently does not have any methods, but you can add methods to handle supplier requests as needed.
}
