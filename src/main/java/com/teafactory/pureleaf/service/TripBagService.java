package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.TripBagDTO;
import com.teafactory.pureleaf.dto.SupplierRequestBagSummaryDTO;
import com.teafactory.pureleaf.entity.*;
import com.teafactory.pureleaf.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class TripBagService {
    @Autowired
    private TripBagRepository tripBagRepository;
    @Autowired
    private TripSupplierRepository tripSupplierRepository;
    @Autowired
    private BagRepository bagRepository;
    @Autowired
    private TeaSupplyRequestRepository teaSupplyRequestRepository;

    public List<TripBagDTO> getAllTripBags() {
        return tripBagRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TripBagDTO getTripBag(Long id) {
        return tripBagRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public TripBagDTO createTripBag(TripBagDTO dto) {
        TripBag tripBag = new TripBag();
        tripBag.setDriverWeight(dto.getDriverWeight());
        tripBag.setWet(dto.getWet());
        tripBag.setCoarse(dto.getCoarse());
        tripBag.setType(dto.getType());
        tripBag.setStatus("pending");

        TripSupplier tripSupplier = tripSupplierRepository.findById(new TripSupplierId(dto.getTripId(), dto.getSupplyRequestId()))
                .orElseThrow(() -> new RuntimeException("TripSupplier not found"));
        tripBag.setTripSupplier(tripSupplier);

        Bag bag = bagRepository.findById(new Bag.BagId(dto.getBagNumber(), dto.getRouteId()))
                .orElseThrow(() -> new RuntimeException("Bag not found"));
        tripBag.setBag(bag);

        TripBag saved = tripBagRepository.save(tripBag);

        // Update bag status to 'assigned'
        bag.setStatus("assigned");
        bagRepository.save(bag);

        // Update trip_supplier status to 'completed' and completionTime
        tripSupplier.setStatus("completed");
        tripSupplier.setCompletionTime(java.time.LocalTime.now());
        tripSupplierRepository.save(tripSupplier);

        // Update tea_supply_request status to 'collected'
        TeaSupplyRequest teaSupplyRequest = teaSupplyRequestRepository.findById(dto.getSupplyRequestId())
                .orElseThrow(() -> new RuntimeException("TeaSupplyRequest not found"));
        teaSupplyRequest.setStatus("collected");
        teaSupplyRequestRepository.save(teaSupplyRequest);

        return convertToDTO(saved);
    }

    public List<TripBagDTO> createTripBags(List<TripBagDTO> dtos) {
        return dtos.stream()
            .map(dto -> {
                if (dto.getWet() == null) dto.setWet(false);
                if (dto.getCoarse() == null) dto.setCoarse(false);
                return createTripBag(dto);
            })
            .collect(Collectors.toList());
    }

    public List<TripBagDTO> getTripBagsBySupplyRequestAndTrip(Long supplyRequestId, Long tripId) {
        return tripBagRepository.findByTripSupplier_TeaSupplyRequest_RequestIdAndTripSupplier_Trip_TripId(supplyRequestId, tripId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SupplierRequestBagSummaryDTO> getSupplierRequestBagSummaryByTripId(Long tripId) {
        List<TripBag> tripBags = tripBagRepository.findAll();
        Map<Long, SupplierRequestBagSummaryDTO> summaryMap = new HashMap<>();
        for (TripBag bag : tripBags) {
            if (bag.getTripSupplier() != null && bag.getTripSupplier().getTrip().getTripId().equals(tripId)) {
                Long supplyRequestId = bag.getTripSupplier().getTeaSupplyRequest().getRequestId();
                SupplierRequestBagSummaryDTO summary = summaryMap.getOrDefault(supplyRequestId, new SupplierRequestBagSummaryDTO(supplyRequestId, 0, 0.0));
                summary.setTotalBags(summary.getTotalBags() + 1);
                summary.setTotalWeight(summary.getTotalWeight() + (bag.getDriverWeight() != null ? bag.getDriverWeight() : 0.0));
                summaryMap.put(supplyRequestId, summary);
            }
        }
        return new java.util.ArrayList<>(summaryMap.values());
    }

    public SupplierRequestBagSummaryDTO getSupplierRequestBagSummaryBySupplyRequestId(Long supplyRequestId) {
        List<TripBag> tripBags = tripBagRepository.findAll();
        int totalBags = 0;
        double totalWeight = 0.0;
        for (TripBag bag : tripBags) {
            if (bag.getTripSupplier() != null && bag.getTripSupplier().getTeaSupplyRequest().getRequestId().equals(supplyRequestId)) {
                totalBags++;
                totalWeight += (bag.getDriverWeight() != null ? bag.getDriverWeight() : 0.0);
            }
        }
        return new SupplierRequestBagSummaryDTO(supplyRequestId, totalBags, totalWeight);
    }

    private TripBagDTO convertToDTO(TripBag tripBag) {
        return new TripBagDTO(
                tripBag.getId(),
                tripBag.getTripSupplier().getTrip().getTripId(),
                tripBag.getTripSupplier().getTeaSupplyRequest().getRequestId(),
                tripBag.getBag().getRoute().getRouteId(),
                tripBag.getBag().getBagNumber(),
                tripBag.getDriverWeight(),
                tripBag.getWet(),
                tripBag.getCoarse(),
                tripBag.getType(),
                tripBag.getStatus()
        );
    }
}
