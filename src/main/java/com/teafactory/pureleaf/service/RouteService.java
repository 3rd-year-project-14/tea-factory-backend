package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.RouteDTO;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.Route;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private FactoryRepository factoryRepository;

    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<RouteDTO> getRouteById(Long id) {
        return routeRepository.findById(id)
                .map(this::convertToDTO);
    }

    public RouteDTO createRoute(RouteDTO routeDTO) {
        Route route = new Route();
        route.setName(routeDTO.getName());
        route.setStartLocation(routeDTO.getStartLocation());
        route.setEndLocation(routeDTO.getEndLocation());
        route.setBagCount(routeDTO.getBagCount());
        route.setDistance(routeDTO.getDistance());
        route.setSupplierCount(routeDTO.getSupplierCount());
        route.setStatus(routeDTO.getStatus());
        route.setCreatedAt(java.time.LocalDateTime.now());
        if (routeDTO.getFactoryId() != null) {
            Factory factory = factoryRepository.findById(routeDTO.getFactoryId())
                .orElseThrow(() -> new RuntimeException("Factory not found"));
            route.setFactory(factory);
        }
        Route savedRoute = routeRepository.save(route);
        return convertToDTO(savedRoute);
    }

    private RouteDTO convertToDTO(Route route) {
        Long factoryId = route.getFactory() != null ? route.getFactory().getFactoryId() : null;
        return new RouteDTO(
                route.getRouteId(),
                route.getName(),
                route.getStartLocation(),
                route.getEndLocation(),
                route.getBagCount(),
                route.getDistance(),
                route.getSupplierCount(),
                route.getStatus(),
                route.getCreatedAt(),
                factoryId
        );
    }
}
