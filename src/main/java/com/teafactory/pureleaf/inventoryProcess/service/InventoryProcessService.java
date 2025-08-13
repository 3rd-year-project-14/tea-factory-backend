package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.entity.Trip;
import com.teafactory.pureleaf.entity.TripBag;
import com.teafactory.pureleaf.inventoryProcess.dto.TripBagDetailsResponse;
import com.teafactory.pureleaf.inventoryProcess.dto.TripsResponse;
import com.teafactory.pureleaf.repository.TripBagRepository;
import com.teafactory.pureleaf.repository.TripRepository;
import com.teafactory.pureleaf.repository.BagWeightRepository;
import com.teafactory.pureleaf.entity.BagWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryProcessService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripBagRepository tripBagRepository;

    @Autowired
    private BagWeightRepository bagWeightRepository;

    public List<TripsResponse> getTodayTripsByFactory(Long factoryId) {
        LocalDate today = LocalDate.now();
        List<Trip> trips = tripRepository.findByRoute_Factory_FactoryIdAndTripDate(factoryId, today);
        return trips.stream().map(trip -> new TripsResponse(
                trip.getTripId(),
                trip.getDriver() != null && trip.getDriver().getUser() != null ? trip.getDriver().getUser().getName() : null,
                trip.getRoute() != null ? trip.getRoute().getRouteId(): null,
                trip.getRoute() != null ? trip.getRoute().getName() : null,
                trip.getTripDate() != null ? trip.getTripDate().toString() : null,
                trip.getStatus(),
                (int) tripBagRepository.countByTripSupplier_Trip_TripId(trip.getTripId())
        )).collect(Collectors.toList());
    }

    public List<TripBagDetailsResponse> getBagsByTripId(Long tripId) {
        List<TripBag> tripBags = tripBagRepository.findByTripSupplier_Trip_TripId(tripId);
        return tripBags.stream().map(tripBag -> {
            Long supplierId = null;
            String supplierName = null;
            Long supplyRequestId = null;
            if (tripBag.getTripSupplier() != null && tripBag.getTripSupplier().getTeaSupplyRequest() != null) {
                if (tripBag.getTripSupplier().getTeaSupplyRequest().getSupplier() != null) {
                    supplierId = tripBag.getTripSupplier().getTeaSupplyRequest().getSupplier().getSupplierId();
                    // Get supplier name from related User entity
                    supplierName = tripBag.getTripSupplier().getTeaSupplyRequest().getSupplier().getUser() != null ?
                        tripBag.getTripSupplier().getTeaSupplyRequest().getSupplier().getUser().getName() : null;
                }
                supplyRequestId = tripBag.getTripSupplier().getTeaSupplyRequest().getRequestId();
            }
            return new TripBagDetailsResponse(
                tripBag.getBag() != null ? tripBag.getBag().getBagNumber() : null,
                supplierId,
                supplierName,
                tripBag.getDriverWeight(),
                supplyRequestId,
                tripBag.getWet(),
                tripBag.getCoarse(),
                tripBag.getId(), // set bagId
                tripBag.getStatus() // set status
            );
        }).collect(Collectors.toList());
    }

    public List<Long> getBagWeightIdsBySupplyRequestAndDate(Long supplyRequestId) {
        List<BagWeight> bagWeights = bagWeightRepository.findBySupplyRequest_RequestIdAndDate(supplyRequestId, java.time.LocalDate.now());
        return bagWeights.stream().map(BagWeight::getId).collect(Collectors.toList());
    }

    public List<TripBagDetailsResponse> getPendingBagsByTripId(Long tripId) {
        return getBagsByTripId(tripId).stream()
                .filter(bag -> "pending".equalsIgnoreCase(bag.getStatus()))
                .collect(Collectors.toList());
    }

    public List<TripBagDetailsResponse> getWeighedBagsByTripId(Long tripId) {
        return getBagsByTripId(tripId).stream()
                .filter(bag -> "weighed".equalsIgnoreCase(bag.getStatus()))
                .collect(Collectors.toList());
    }
}
