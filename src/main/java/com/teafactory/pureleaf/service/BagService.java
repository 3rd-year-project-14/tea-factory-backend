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

    public BagDTO getBagByRouteIdAndBagId(Long routeId, Long bagId) {
        return bagRepository.findById(new Bag.BagId(bagId, routeId))
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<BagDTO> getAllBags() {
        List<Bag> bags = bagRepository.findAll();
        return bags.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BagDTO createBag(BagDTO bagDTO) {
        Bag bag = new Bag();
        bag.setBagId(bagDTO.getBagId());
        Route route = new Route();
        route.setRouteId(bagDTO.getRouteId());
        bag.setRoute(route);
        Bag savedBag = bagRepository.save(bag);
        return convertToDTO(savedBag);
    }

    private BagDTO convertToDTO(Bag bag) {
        return new BagDTO(
                bag.getBagId(),
                bag.getRoute().getRouteId()
        );
    }
}
