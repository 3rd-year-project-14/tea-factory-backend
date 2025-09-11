package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.inventoryProcess.dto.TripBagDetailsResponse;
import com.teafactory.pureleaf.inventoryProcess.dto.TripBagSummaryResponse;
import com.teafactory.pureleaf.inventoryProcess.dto.WeighingSummaryResponse;
import com.teafactory.pureleaf.inventoryProcess.entity.*;
import com.teafactory.pureleaf.inventoryProcess.repository.TripBagRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.TripRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.WeighingSessionRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.TeaSupplyRequestRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.BagRepository;
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

    public TripBagSummaryResponse getTodayTripBagSummary(Long tripId) {
        LocalDate today = LocalDate.now();
        String status = "pending";
        long totalBags = tripBagRepository
                .countByTripSupplier_Trip_TripIdAndTripSupplier_Trip_TripDateAndStatus(tripId, today, status);
        long supplierRequestCount = tripBagRepository
                .countDistinctSupplyRequestsByTripIdAndDateAndStatus(tripId, today, status);
        Double totalWeight = tripBagRepository
                .sumDriverWeightByTripIdAndDateAndStatus(tripId, today, status);

        WeighingSession session = weighingSessionRepository
                .findFirstByTrip_TripIdAndSessionDateOrderByStartTimeDesc(tripId, today);
        Long sessionId = session != null ? session.getSessionId() : null;
        Long userId = (session != null && session.getUser() != null) ? session.getUser().getId() : null;

        return TripBagSummaryResponse.builder()
                .tripId(tripId)
                .sessionId(sessionId)
                .userId(userId)
                .supplierRequestCount(supplierRequestCount)
                .totalBags(totalBags)
                .totalWeight(totalWeight != null ? totalWeight : 0.0)
                .build();
    }

    public com.teafactory.pureleaf.inventoryProcess.dto.WeighingSummaryResponse getTodayWeighingSummaryByTripAndStatus(Long tripId, String status) {
        LocalDate today = LocalDate.now();
        BagWeightRepository.TodayWeighingSummaryProjection p = bagWeightRepository.getTodayWeighingSummaryByTripAndStatus(tripId, status, today);
        long totalSuppliers = p != null && p.getTotalSuppliers() != null ? p.getTotalSuppliers() : 0L;
        long totalBags = p != null && p.getTotalBags() != null ? p.getTotalBags() : 0L;
        double totalGrossWeight = p != null && p.getTotalGrossWeight() != null ? p.getTotalGrossWeight() : 0.0;
        return new WeighingSummaryResponse(totalSuppliers, totalBags, totalGrossWeight);
    }
}
