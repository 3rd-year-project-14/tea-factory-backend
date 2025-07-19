package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.RouteDTO;
import com.teafactory.pureleaf.entity.Route;
import com.teafactory.pureleaf.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    public List<RouteDTO> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        return routes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public RouteDTO getRouteById(Long id) {
        return routeRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    private RouteDTO convertToDTO(Route route) {
        RouteDTO dto = new RouteDTO();
        dto.setRouteId(route.getRouteId());
        dto.setName(route.getName());
        dto.setStartLocation(route.getStartLocation());
        dto.setEndLocation(route.getEndLocation());
        dto.setBagCount(route.getBagCount());
        dto.setDistance(route.getDistance());
        dto.setSupplierCount(route.getSupplierCount());
        dto.setStatus(route.getStatus());
        dto.setCreatedAt(route.getCreatedAt());
        if (route.getFactory() != null) {
            dto.setFactoryId(route.getFactory().getFactoryId());
        }
        return dto;
    }
}

