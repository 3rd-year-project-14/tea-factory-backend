package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.BagDTO;
import com.teafactory.pureleaf.entity.Bag;
import com.teafactory.pureleaf.entity.Route;
import com.teafactory.pureleaf.repository.BagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BagService {
    @Autowired
    private BagRepository bagRepository;

    public List<BagDTO> getBagsByRouteId(Long routeId) {
        List<Bag> bags = bagRepository.findByRoute_RouteId(routeId);
        return bags.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BagDTO getBagByRouteIdAndBagNumber(Long routeId, String bagNumber) {
        return bagRepository.findById(new Bag.BagId(bagNumber, routeId))
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<BagDTO> getAllBags() {
        List<Bag> bags = bagRepository.findAll();
        return bags.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BagDTO createBag(BagDTO bagDTO) {
        Bag bag = new Bag();
        bag.setBagNumber(bagDTO.getBagNumber());
        bag.setRouteId(bagDTO.getRouteId()); // <-- Ensure routeId is set
        Route route = new Route();
        route.setRouteId(bagDTO.getRouteId());
        bag.setRoute(route);
        // Set status to 'not-assigned' by default if not provided
        if (bagDTO.getStatus() == null || bagDTO.getStatus().isEmpty()) {
            bag.setStatus("not-assigned");
        } else {
            bag.setStatus(bagDTO.getStatus());
        }
        Bag savedBag = bagRepository.save(bag);
        return convertToDTO(savedBag);
    }

    public List<String> getNotAssignedBagNumbersByRouteId(Long routeId) {
        List<Bag> bags = bagRepository.findByRoute_RouteIdAndStatus(routeId, "not-assigned");
        return bags.stream().map(Bag::getBagNumber).collect(Collectors.toList());
    }

    private BagDTO convertToDTO(Bag bag) {
        return new BagDTO(
                bag.getBagNumber(), // Use getBagNumber instead of getBagId
                bag.getRoute().getRouteId(),
                bag.getStatus() // Add status to DTO if needed
        );
    }
}
