package com.teafactory.pureleaf.routes.service;

import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.routes.dto.CreateRouteDTO;
import com.teafactory.pureleaf.routes.dto.RouteDetailsDTO;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.driverProcess.repository.DriverRepository;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private DriverRepository driverRepository;

    // Parses a latitude and longitude string into a double array
    private double[] parseLatLng(String mapsUrl) {
        try {
            String[] parts = mapsUrl.split("\\?q=");
            if (parts.length < 2) return null;
            String[] latlng = parts[1].split(",");
            double lat = Double.parseDouble(latlng[0]);
            double lng = Double.parseDouble(latlng[1]);
            return new double[]{lat, lng};
        } catch (Exception e) {
            return null; // return null if parsing fails
        }
    }

    // Calculates the distance between two coordinates using the Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in km
    }

    // Retrieves route details for a given factory ID
    public List<RouteDetailsDTO> findRoutesDetailsByFactoryId(Long factoryId) {
        return routeRepository.findRouteDetailsByFactoryId(factoryId);
    }

    // Creates a new route using the provided DTO
    public Route createRoute(CreateRouteDTO createRouteDTO) {
        Factory factory = factoryRepository.findById(createRouteDTO.getFactoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Factory not found with id: " + createRouteDTO.getFactoryId()));

        double[] start = parseLatLng(createRouteDTO.getStartLocation());
        double[] end = parseLatLng(createRouteDTO.getEndLocation());
        System.out.println("Start coordinates: " + (start != null ? start[0] + ", " + start[1] : "null"));
        double distance = 0;


        if (start != null && end != null) {
            distance = calculateDistance(start[0], start[1], end[0], end[1]);
            System.out.println("Calculated distance: " + distance + " km");
        }

        Route route = Route.builder()
                .name(createRouteDTO.getName())
                .startLocation(createRouteDTO.getStartLocation())
                .endLocation(createRouteDTO.getEndLocation())
                .routeCode(createRouteDTO.getRouteCode())
                .distance(distance)
                .status(true)
                .createdAt(LocalDateTime.now())
                .factory(factory)
                .build();

        return routeRepository.save(route);
    }

}
