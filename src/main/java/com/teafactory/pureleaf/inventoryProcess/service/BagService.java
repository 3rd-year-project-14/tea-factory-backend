package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.inventoryProcess.dto.BagDTO;
import com.teafactory.pureleaf.inventoryProcess.entity.Bag;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.inventoryProcess.repository.BagRepository;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class BagService {
    @Autowired
    private BagRepository bagRepository;
    @Autowired
    private RouteRepository routeRepository;

    public List<BagDTO> getBagsByRouteId(Long routeId) {
        return bagRepository.findByRoute_RouteId(routeId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BagDTO getBagByRouteIdAndBagNumber(Long routeId, String bagNumber) {
        return bagRepository.findById(new Bag.BagId(bagNumber, routeId))
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<BagDTO> getAllBags() {
        return bagRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BagDTO createBag(BagDTO bagDTO) {
        Bag bag = new Bag();
        bag.setBagNumber(bagDTO.getBagNumber());
        bag.setRouteId(bagDTO.getRouteId());
        Route route = new Route();
        route.setRouteId(bagDTO.getRouteId());
        bag.setRoute(route);
        if (bagDTO.getStatus() == null || bagDTO.getStatus().isEmpty()) {
            bag.setStatus("not-assigned");
        } else {
            bag.setStatus(bagDTO.getStatus());
        }
        return convertToDTO(bagRepository.save(bag));
    }

    public List<String> getNotAssignedBagNumbersByRouteId(Long routeId) {
        return bagRepository.findByRoute_RouteIdAndStatus(routeId, "not-assigned").stream().map(Bag::getBagNumber).collect(Collectors.toList());
    }

    private BagDTO convertToDTO(Bag bag) {
        return new BagDTO(
                bag.getBagNumber(),
                bag.getRoute().getRouteId(),
                bag.getStatus()
        );
    }

    @Transactional
    public List<BagDTO> generateBagsForRoute(Long routeId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (quantity > 1000) { // arbitrary upper limit to prevent accidental huge inserts
            throw new IllegalArgumentException("Quantity too large. Max allowed is 1000");
        }

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));

        String maxBagNumber = bagRepository.findMaxBagNumberByRouteId(routeId);
        int start = (maxBagNumber == null) ? 1 : Integer.parseInt(maxBagNumber) + 1;

        List<Bag> newBags = new ArrayList<>(quantity);
        for (int i = 0; i < quantity; i++) {
            int current = start + i;
            String bagNumber = String.format("%03d", current); // zero-padded (handles > 999 gracefully)
            Bag bag = new Bag();
            bag.setBagNumber(bagNumber);
            bag.setRouteId(routeId);
            bag.setRoute(route);
            bag.setStatus("not-assigned");
            newBags.add(bag);
        }
        List<Bag> saved = bagRepository.saveAll(newBags);
        return saved.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
