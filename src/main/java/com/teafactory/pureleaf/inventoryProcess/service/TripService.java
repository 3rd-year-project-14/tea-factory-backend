package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.inventoryProcess.dto.TripDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.TripStatusCountsDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.TripSummaryResponseDTO;
import com.teafactory.pureleaf.driverProcess.entity.Driver;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.inventoryProcess.entity.Trip;
import com.teafactory.pureleaf.driverProcess.repository.DriverRepository;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.TripRepository;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
    private final RouteRepository routeRepository;
    private final FactoryRepository factoryRepository;
    private final BagWeightRepository bagWeightRepository;

    public TripService(TripRepository tripRepository, DriverRepository driverRepository, RouteRepository routeRepository, FactoryRepository factoryRepository, BagWeightRepository bagWeightRepository) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.routeRepository = routeRepository;
        this.factoryRepository = factoryRepository;
        this.bagWeightRepository = bagWeightRepository;
    }

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

    // Get counts of trips by status for today for a given factory
    public TripStatusCountsDTO getTodayTripStatusCounts(Long factoryId) {
        // Ensure factory exists
        factoryRepository.findById(factoryId).orElseThrow(() -> new ResourceNotFoundException("Factory not found with id: " + factoryId));
        LocalDate today = LocalDate.now();

        TripRepository.TripStatusAggregation aggregation = tripRepository.aggregateStatusCounts(factoryId, today);
        long pendingCount = aggregation != null ? aggregation.getPendingCount() : 0L;
        long arrivedCount = aggregation != null ? aggregation.getArrivedCount() : 0L;
        long weighedCount = aggregation != null ? aggregation.getWeighedCount() : 0L;
        long completedCount = aggregation != null ? aggregation.getCompletedCount() : 0L;

        return new TripStatusCountsDTO(factoryId, today, pendingCount, arrivedCount, weighedCount, completedCount);
    }

    // Get paginated trip summaries for today filtered by status and optional search term
    public Page<TripSummaryResponseDTO> getTodayTripSummaries(Long factoryId,
                                                              String status,
                                                              String search,
                                                              Pageable pageable) {
        factoryRepository.findById(factoryId).orElseThrow(() -> new ResourceNotFoundException("Factory not found with id: " + factoryId));

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status must not be empty");
        }
        String normalized = status.toLowerCase().trim();

        java.util.Set<String> allowed = java.util.Set.of("pending","arrived","weighed","completed");
        if (!allowed.contains(normalized)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Allowed: " + allowed);
        }
        java.time.LocalDate today = java.time.LocalDate.now();

        Page<TripRepository.TripSummaryProjection> projectionsPage = tripRepository.findTripSummaries(factoryId, normalized, today, search, pageable);

        boolean weighedStatus = "weighed".equals(normalized);
        if (weighedStatus && !projectionsPage.isEmpty()) {
            List<TripRepository.TripSummaryProjection> content = projectionsPage.getContent();
            List<Long> tripIds = content.stream().map(TripRepository.TripSummaryProjection::getTripId).toList();
            java.util.Map<Long, Double> grossMap = bagWeightRepository.sumGrossWeightByTripIds(tripIds).stream()
                    .collect(java.util.stream.Collectors.toMap(BagWeightRepository.TripGrossWeightProjection::getTripId, BagWeightRepository.TripGrossWeightProjection::getTotalGrossWeight));
            return projectionsPage.map(p -> new TripSummaryResponseDTO(
                    p.getTripId(), p.getRouteId(), p.getRouteName(), p.getDriverName(), p.getBagCount(),
                    grossMap.getOrDefault(p.getTripId(), 0d),
                    null
            ));
        }

        boolean completedStatus = "completed".equals(normalized);
        if (completedStatus && !projectionsPage.isEmpty()) {
            List<TripRepository.TripSummaryProjection> content = projectionsPage.getContent();
            List<Long> tripIds = content.stream().map(TripRepository.TripSummaryProjection::getTripId).toList();
            java.util.Map<Long, Double> grossMap = bagWeightRepository.sumGrossWeightByTripIds(tripIds).stream()
                    .collect(java.util.stream.Collectors.toMap(BagWeightRepository.TripGrossWeightProjection::getTripId, BagWeightRepository.TripGrossWeightProjection::getTotalGrossWeight));
            java.util.Map<Long, Double> tareMap = bagWeightRepository.sumTareWeightByTripIds(tripIds).stream()
                    .collect(java.util.stream.Collectors.toMap(BagWeightRepository.TripTareWeightProjection::getTripId, BagWeightRepository.TripTareWeightProjection::getTotalTareWeight));
            return projectionsPage.map(p -> new TripSummaryResponseDTO(
                    p.getTripId(), p.getRouteId(), p.getRouteName(), p.getDriverName(), p.getBagCount(),
                    grossMap.getOrDefault(p.getTripId(), 0d),
                    tareMap.getOrDefault(p.getTripId(), 0d)
            ));
        }
        return projectionsPage.map(p -> new TripSummaryResponseDTO(
                p.getTripId(), p.getRouteId(), p.getRouteName(), p.getDriverName(), p.getBagCount(),
                null,
                null
        ));
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
