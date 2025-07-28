package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.auth.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teafactory.pureleaf.repository.SupplierRequestRepo;
import com.teafactory.pureleaf.repository.SupplierRepository;
import com.teafactory.pureleaf.entity.SupplierRequest;
import com.teafactory.pureleaf.entity.Supplier;
import com.teafactory.pureleaf.entity.Route;
import com.teafactory.pureleaf.repository.RouteRepository;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.repository.UserRepository;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.repository.FactoryRepository;
import java.time.LocalDate;

@Service
public class SupplierService {
    @Autowired
    private SupplierRequestRepo supplierRequestRepo;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FactoryRepository factoryRepository;

    public Supplier approveSupplierRequest(Long requestId, Long routeId, Integer initialBagCount) {
        SupplierRequest request = supplierRequestRepo.findById(requestId).orElse(null);
        if (request == null) return null;

        Route route = routeRepository.findById(routeId).orElse(null);
        if (route == null) return null;

        User user = request.getUser();
        User managedUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        managedUser.setRole(Role.SUPPLIER);
        managedUser.setFactory(request.getFactory());
        userRepository.save(managedUser);
        Supplier supplier = new Supplier();
        supplier.setUser(managedUser);
        supplier.setRoute(route);
        supplier.setLandSize(request.getLandSize());
        supplier.setLandLocation(request.getLandLocation());
        supplier.setPickupLocation(request.getPickupLocation());
        supplier.setNicImage(request.getNicImage());
        supplier.setApprovedDate(LocalDate.now());
        supplier.setIsActive(true);
        supplier.setSupplierRequestId(requestId);
        supplier.setFactory(request.getFactory());
        supplier.setInitialBagCount(initialBagCount); // Make sure this is set from the query param

        Factory factory = request.getFactory();
        if (factory != null && factory.getFactoryId() != null) {
            factory = factoryRepository.findById(factory.getFactoryId())
                .orElseThrow(() -> new RuntimeException("Factory not found"));
            supplier.setFactory(factory);
        }

        Supplier savedSupplier = supplierRepository.save(supplier);
        supplierRequestRepo.deleteById(requestId);
        return savedSupplier;
    }
}
