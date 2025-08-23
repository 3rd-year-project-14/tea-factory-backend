package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.entity.*;
import com.teafactory.pureleaf.inventoryProcess.dto.TripBagDetailsResponse;
import com.teafactory.pureleaf.inventoryProcess.dto.TripsResponse;
import com.teafactory.pureleaf.repository.TripBagRepository;
import com.teafactory.pureleaf.repository.TripRepository;
import com.teafactory.pureleaf.repository.BagWeightRepository;
import com.teafactory.pureleaf.repository.WeighingSessionRepository;
import com.teafactory.pureleaf.repository.TeaSupplyRequestRepository;
import com.teafactory.pureleaf.repository.BagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryProcessService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripBagRepository tripBagRepository;

    @Autowired
    private BagWeightRepository bagWeightRepository;

    @Autowired
    private WeighingSessionRepository weighingSessionRepository;

    @Autowired
    private TeaSupplyRequestRepository teaSupplyRequestRepository;

    @Autowired
    private BagRepository bagRepository;

    public List<TripsResponse> getTodayTripsByFactory(Long factoryId) {
        LocalDate today = LocalDate.now();
        List<Trip> trips = tripRepository.findByRoute_Factory_FactoryIdAndTripDate(factoryId, today);
        return trips.stream().map(trip -> {
            // Get the single weighing session for this trip
            WeighingSession session = weighingSessionRepository.findFirstByTrip_TripId(trip.getTripId());
            Long sessionId = null;
            Double totalGrossWeight = null;
            Double totalTareWeight = null;
            if (session != null) {
                sessionId = session.getSessionId();
                totalGrossWeight = bagWeightRepository.sumGrossWeightBySessionId(sessionId);
                totalTareWeight = bagWeightRepository.sumTareWeightBySessionId(sessionId);
            }
            return new TripsResponse(
                trip.getTripId(),
                trip.getDriver() != null && trip.getDriver().getUser() != null ? trip.getDriver().getUser().getName() : null,
                trip.getRoute() != null ? trip.getRoute().getRouteId(): null,
                trip.getRoute() != null ? trip.getRoute().getName() : null,
                trip.getTripDate() != null ? trip.getTripDate().toString() : null,
                trip.getStatus(),
                (int) tripBagRepository.countByTripSupplier_Trip_TripId(trip.getTripId()),
                sessionId,
                totalGrossWeight,
                totalTareWeight
            );
        }).collect(Collectors.toList());
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

    @Transactional
    public void updateTareWeightAndCompleteProcess(Long bagWeightId, Double tareWeight) {
        try {
            BagWeight bagWeight = bagWeightRepository.findById(bagWeightId)
                    .orElseThrow(() -> new RuntimeException("BagWeight not found"));
            bagWeight.setTareWeight(tareWeight);
            double netWeight = bagWeight.getGrossWeight() - bagWeight.getWater() - bagWeight.getCoarse() - bagWeight.getOtherWeight() - tareWeight;
            bagWeight.setNetWeight(netWeight);
            bagWeightRepository.save(bagWeight);

            Long supplyRequestId = bagWeight.getSupplyRequest().getRequestId();
            List<TripBag> tripBags = tripBagRepository.findByTripSupplier_TeaSupplyRequest_RequestId(supplyRequestId);
            for (TripBag tripBag : tripBags) {
                tripBag.setStatus("completed");
                tripBagRepository.save(tripBag);
                // Update Bag status to "not-assigned" using bagNumber and routeId
                String bagNumber = tripBag.getBag() != null ? tripBag.getBag().getBagNumber() : null;
                Long routeId = tripBag.getTripSupplier() != null && tripBag.getTripSupplier().getTrip() != null && tripBag.getTripSupplier().getTrip().getRoute() != null ? tripBag.getTripSupplier().getTrip().getRoute().getRouteId() : null;
                if (bagNumber != null && routeId != null) {
                    Bag.BagId bagId = new Bag.BagId(bagNumber, routeId);
                    Bag bag = bagRepository.findById(bagId).orElse(null);
                    if (bag != null) {
                        bag.setStatus("not-assigned");
                        bagRepository.save(bag);
                    }
                }
            }

            TeaSupplyRequest teaSupplyRequest = teaSupplyRequestRepository.findById(supplyRequestId)
                    .orElseThrow(() -> new RuntimeException("TeaSupplyRequest not found"));
            teaSupplyRequest.setStatus("completed");
            teaSupplyRequestRepository.save(teaSupplyRequest);

            // Retrieve sessionId from BagWeight
            Long sessionId = bagWeight.getWeighingSession() != null ? bagWeight.getWeighingSession().getSessionId() : null;
            if (sessionId == null) {
                throw new RuntimeException("SessionId not found in BagWeight");
            }
            // Check if all BagWeight records for this sessionId have non-null netWeight
            List<BagWeight> sessionBagWeights = bagWeightRepository.findByWeighingSession_SessionId(sessionId);
            boolean allNetWeightsPresent = sessionBagWeights.stream().allMatch(bw -> bw.getNetWeight() != 0);
            if (allNetWeightsPresent) {
                // Update WeighingSession status to completed and set end time
                WeighingSession session = weighingSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new RuntimeException("WeighingSession not found"));
                session.setStatus("completed");
                session.setEndTime(java.time.LocalTime.now());
                weighingSessionRepository.save(session);

                // Update Trip status to completed if session is completed
                Trip trip = session.getTrip();
                if (trip != null) {
                    trip.setStatus("completed");
                    tripRepository.save(trip);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to complete process: " + e.getMessage(), e);
        }
    }

}
