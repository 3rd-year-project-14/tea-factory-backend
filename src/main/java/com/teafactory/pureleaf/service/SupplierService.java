package com.teafactory.pureleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teafactory.pureleaf.repository.SupplierRequestRepo;
import com.teafactory.pureleaf.repository.SupplierRepository;
import com.teafactory.pureleaf.entity.SupplierRequest;
import com.teafactory.pureleaf.entity.Supplier;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SupplierService {
    @Autowired
    private SupplierRequestRepo supplierRequestRepo;
    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier approveSupplierRequest(Long requestId, Long routeId) {
        SupplierRequest request = supplierRequestRepo.findById(requestId).orElse(null);
        if (request == null) return null;

        Supplier supplier = new Supplier();
        supplier.setUser(request.getUser());
        supplier.setRouteId(routeId);
        supplier.setLandSize(request.getLandSize());
        supplier.setLandLocation(request.getLandLocation());
        supplier.setPickupLocation(request.getPickupLocation());
        supplier.setNicImage(request.getNicImage());
        supplier.setApprovedDate(LocalDate.now()); // Use LocalDate for approvedDate
        supplier.setIsActive(true);
        supplier.setSupplierRequestId(requestId);

        Supplier savedSupplier = supplierRepository.save(supplier);
        supplierRequestRepo.deleteById(requestId);
        return savedSupplier;
    }
}
