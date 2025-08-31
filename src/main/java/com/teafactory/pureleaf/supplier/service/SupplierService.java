package com.teafactory.pureleaf.supplier.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teafactory.pureleaf.auth.entity.Role;
import com.teafactory.pureleaf.entity.*;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.repository.*;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import com.teafactory.pureleaf.supplier.dto.SupplierCountDTO;
import com.teafactory.pureleaf.supplier.dto.ActiveSuppliersDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierDetailsDTO;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import com.teafactory.pureleaf.supplier.repository.SupplierRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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
        // Calculate and set pickupToRouteStartDistance using road distance
        double[] pickupCoords = extractLatLng(request.getPickupLocation());
        double[] routeCoords = extractLatLng(route.getStartLocation());
        Double distance = null;
        if (pickupCoords != null && routeCoords != null) {
            distance = getRoadDistance(pickupCoords, routeCoords);
        }
        supplier.setPickupToRouteStartDistance(distance);
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
