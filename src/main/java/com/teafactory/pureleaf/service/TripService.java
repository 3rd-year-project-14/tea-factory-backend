package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.TripDTO;
import com.teafactory.pureleaf.entity.Driver;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.entity.Trip;
import com.teafactory.pureleaf.repository.DriverRepository;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import com.teafactory.pureleaf.repository.TripRepository;
import com.teafactory.pureleaf.repository.TripSupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TripSupplierRepository tripSupplierRepository;

    public List<TripDTO> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TripDTO> getTripById(Long id) {
        return tripRepository.findById(id)
                .map(this::convertToDTO);
    }

    public TripDTO createTrip(Long driverId, Long routeId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new RuntimeException("Route not found"));
        Trip trip = new Trip();
        trip.setDriver(driver);
        trip.setRoute(route);
        trip.setTripDate(LocalDate.now());
        trip.setStartTime(LocalTime.now());
        trip.setStatus("pending");
        Trip savedTrip = tripRepository.save(trip);
        return convertToDTO(savedTrip);
    }

    public Optional<TripDTO> getTodayTripByDriverId(Long driverId) {
        LocalDate today = LocalDate.now();
        return tripRepository.findByDriver_DriverIdAndTripDate(driverId, today)
                .map(this::convertToDTO);
    }

    public Optional<TripDTO> updateTripStatus(Long tripId, String status) {
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        if (tripOpt.isPresent()) {
            Trip trip = tripOpt.get();
            trip.setStatus(status);
            if ("complete".equalsIgnoreCase(status) || "completed".equalsIgnoreCase(status)) {
                trip.setEndTime(LocalTime.now());
            }
            tripRepository.save(trip);
            return Optional.of(convertToDTO(trip));
        }
        return Optional.empty();
    }

    private TripDTO convertToDTO(Trip trip) {
        return new TripDTO(
                trip.getTripId(),
                trip.getDriver() != null ? trip.getDriver().getDriverId() : null,
                trip.getRoute() != null ? trip.getRoute().getRouteId() : null,
                trip.getTripDate(),
                trip.getStartTime(),
                trip.getEndTime(),
                trip.getStatus()
        );
    }
}
