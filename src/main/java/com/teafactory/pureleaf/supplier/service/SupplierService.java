package com.teafactory.pureleaf.supplier.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teafactory.pureleaf.auth.entity.Role;
import com.teafactory.pureleaf.entity.*;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.repository.*;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import com.teafactory.pureleaf.supplier.dto.ApproveSupplierRequestDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierCountDTO;
import com.teafactory.pureleaf.supplier.dto.ActiveSuppliersDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierDetailsDTO;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import com.teafactory.pureleaf.supplier.repository.SupplierRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRequestRepository supplierRequestRepo;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FactoryRepository factoryRepository;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;
    @Autowired
    private SupplierRequestRepository supplierRequestRepository;

    private double[] extractLatLng(String url) {
        // Example: https://maps.google.com/?q=6.321426274959198,80.43685999549393
        try {
            int idx = url.indexOf("?q=");
            if (idx == -1) return null;
            String coords = url.substring(idx + 3);
            String[] parts = coords.split(",");
            if (parts.length != 2) return null;
            double lat = Double.parseDouble(parts[0]);
            double lng = Double.parseDouble(parts[1]);
            return new double[]{lat, lng};
        } catch (Exception e) {
            return null;
        }
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in km
    }

    private Double getRoadDistance(double[] origin, double[] destination) {
        String origins = origin[0] + "," + origin[1];
        String destinations = destination[0] + "," + destination[1];
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origins + "&destinations=" + destinations + "&key=" + googleMapsApiKey;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode element = root.path("rows").get(0).path("elements").get(0);
            if (element.path("status").asText().equals("OK")) {
                double meters = element.path("distance").path("value").asDouble();
                return meters / 1000.0; // Convert to km
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }

    @Transactional
    public void approveSupplierRequest(Long supplierRequestId, ApproveSupplierRequestDTO dto) {
        // 1. Find and validate supplier request
        SupplierRequest request = supplierRequestRepo.findById(supplierRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier request not found with id: " + supplierRequestId));

        // 2. Find and validate route
        Route route = routeRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + dto.getRouteId()));

        // 3. Get the requesting user and update role → SUPPLIER
        User managedUser = userRepository.findById(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUser().getId()));

        // 4. Get factory if provided
        Factory factory = null;
        if (request.getFactory() != null && request.getFactory().getFactoryId() != null) {
            factory = factoryRepository.findById(request.getFactory().getFactoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Factory not found with id: " + request.getFactory().getFactoryId()));
        }


        managedUser.setRole(Role.SUPPLIER);
        managedUser.setUpdatedAt(LocalDateTime.now());
        managedUser.setFactory(factory);
        userRepository.save(managedUser);

        // 5. Create new Supplier entity
        Supplier supplier = new Supplier();
        supplier.setUser(managedUser);
        supplier.setRoute(route);
        supplier.setLandSize(request.getLandSize());
        supplier.setLandLocation(request.getLandLocation());
        supplier.setPickupLocation(request.getPickupLocation());
        supplier.setNicImage(request.getNicImage());
        supplier.setApprovedDate(LocalDate.now());
        supplier.setIsActive(true);
        supplier.setInitialBagCount(dto.getInitialBagCount());
        supplier.setFactory(factory);

        // 6. Calculate pickup → route start distance
        double[] pickupCoords = extractLatLng(request.getPickupLocation());
        double[] routeCoords = extractLatLng(route.getStartLocation());
        if (pickupCoords != null && routeCoords != null) {
            supplier.setPickupToRouteStartDistance(
                    getRoadDistance(pickupCoords, routeCoords)
            );
        }

        // 7. Save Supplier + delete request
        supplierRepository.save(supplier);
        supplierRequestRepo.delete(request);

        // 8. Update route bagCount and supplierCount
        Integer currentBagCount = route.getBagCount() != null ? route.getBagCount() : 0;
        Integer initialBagCount = dto.getInitialBagCount() != null ? dto.getInitialBagCount() : 0;
        route.setBagCount(currentBagCount + initialBagCount);
        route.setSupplierCount(route.getSupplierCount() != null ? route.getSupplierCount() + 1 : 1);
        routeRepository.save(route);

    }


    public List<ActiveSuppliersDTO> getActiveSuppliersByFactoryId(Long factoryId) {
        Factory factory = factoryRepository.findById(factoryId).orElse(null);
        if (factory == null) {
            throw new ResourceNotFoundException("Factory not found with id: " + factoryId);
        }
        List<ActiveSuppliersDTO> suppliers = supplierRepository.findSupplierDetailsByFactoryId(factoryId);
        if (suppliers == null || suppliers.isEmpty()) {
            throw new ResourceNotFoundException("No suppliers found for factoryId: " + factoryId);
        }
        return suppliers;
    }

    public SupplierCountDTO getSuppliersCounts( Long factoryId) {
        // Check if factory exists
        if (!factoryRepository.existsById(factoryId)) {
            throw new ResourceNotFoundException("Factory not found with id: " + factoryId);
        }
        Long activeSupplierCount = supplierRepository.countByIsActiveIsTrueAndFactory_factoryId(factoryId);
        Long pendingRequestCount = supplierRequestRepository.countByStatusAndFactory_factoryId("pending", factoryId);
        Long rejectedRequestCount = supplierRequestRepository.countByStatusAndFactory_factoryId("rejected", factoryId);
        return new SupplierCountDTO(activeSupplierCount, pendingRequestCount, rejectedRequestCount);
    }

    public SupplierDetailsDTO getSupplierDetails (Long supplierId) {
        if (!supplierRepository.existsById((supplierId))) {
            throw new ResourceNotFoundException("Supplier not found with id: " + supplierId);
        }
        SupplierDetailsDTO supplierDetails = supplierRepository.findSupplierDetails(supplierId);
        return supplierDetails;
    }



}
