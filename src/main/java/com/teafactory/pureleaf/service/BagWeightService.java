package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.BagWeightDTO;
import com.teafactory.pureleaf.entity.BagWeight;
import com.teafactory.pureleaf.entity.TeaSupplyRequest;
import com.teafactory.pureleaf.entity.Trip;
import com.teafactory.pureleaf.entity.TripBag;
import com.teafactory.pureleaf.entity.WeighingSession;
import com.teafactory.pureleaf.repository.BagWeightRepository;
import com.teafactory.pureleaf.repository.TeaSupplyRequestRepository;
import com.teafactory.pureleaf.repository.TripBagRepository;
import com.teafactory.pureleaf.repository.TripRepository;
import com.teafactory.pureleaf.repository.WeighingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BagWeightService {

    @Autowired
    private BagWeightRepository bagWeightRepository;
    @Autowired
    private TeaSupplyRequestRepository teaSupplyRequestRepository;
    @Autowired
    private WeighingSessionRepository weighingSessionRepository;
    @Autowired
    private TripBagRepository tripBagRepository;
    @Autowired
    private TripRepository tripRepository;

    public List<BagWeight> getAllBagWeights() {
        return bagWeightRepository.findAll();
    }

    public Optional<BagWeight> getBagWeightById(Long id) {
        return bagWeightRepository.findById(id);
    }

    public BagWeight createBagWeight(BagWeightDTO dto) {
        TeaSupplyRequest supplyRequest = teaSupplyRequestRepository.findById(dto.getSupplyRequestId())
                .orElseThrow(() -> new RuntimeException("SupplyRequest not found"));
        WeighingSession weighingSession = weighingSessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("WeighingSession not found"));
        System.out.println( "weighingSession id: " + weighingSession.getSessionId());;

        BagWeight bagWeight = new BagWeight();

        bagWeight.setCoarse(dto.getCoarse());
        bagWeight.setWater(dto.getWater());
        bagWeight.setGrossWeight(dto.getGrossWeight());
        bagWeight.setSupplyRequest(supplyRequest);
        bagWeight.setWeighingSession(weighingSession);
        bagWeight.setDate(LocalDate.now());
        bagWeight.setRecordedAt(LocalDateTime.now());
        bagWeight.setOtherWeight(dto.getOtherWeight());
        bagWeight.setReason(dto.getReason());
        if (dto.getBagNumbers() != null) {
            bagWeight.setBagTotal(dto.getBagNumbers().size());
        } else {
            bagWeight.setBagTotal(0);
        }
        // netWeight, tareWeight can be set if needed
        BagWeight saved = bagWeightRepository.save(bagWeight);
        // Update TripBag status to 'weighed' only for the sent bag numbers and supplyRequestId
        updateTripBagStatusToWeighed(dto.getSupplyRequestId(), dto.getBagNumbers());
        updateSupplyRequestStatusIfAllBagsWeighed(dto.getSupplyRequestId());

        // NEW LOGIC: Check if all bags for the trip are weighed, then update session and trip status
        Long tripId = weighingSession.getTrip().getTripId();
        List<TripBag> tripBagsForTrip = tripBagRepository.findByTripSupplier_Trip_TripId(tripId);
        boolean allWeighedForTrip = tripBagsForTrip.stream().allMatch(bag -> "weighed".equalsIgnoreCase(bag.getStatus()));
        if (allWeighedForTrip) {
            // Update WeighingSession status and endTime
            List<WeighingSession> sessions = weighingSessionRepository.findByTrip_TripId(tripId);
            for (WeighingSession session : sessions) {
                session.setStatus("completed");
                session.setEndTime(java.time.LocalTime.now());
            }
            weighingSessionRepository.saveAll(sessions);
            // Update Trip status
            Trip trip = weighingSession.getTrip();
            trip.setStatus("completed");
            tripRepository.save(trip);
        }
        return saved;
    }



    public void updateTripBagStatusToWeighed(Long supplyRequestId, List<String> bagNumbers) {
        if (supplyRequestId == null || bagNumbers == null || bagNumbers.isEmpty()) return;
        System.out.println("Updating TripBag status for supplyRequestId: " + supplyRequestId + ", bagNumbers: " + bagNumbers);
        List<TripBag> tripBags = tripBagRepository.findByTripSupplier_TeaSupplyRequest_RequestIdAndBag_BagNumberIn(supplyRequestId, bagNumbers);
        for (TripBag tripBag : tripBags) {
            tripBag.setStatus("weighed");
        }
        tripBagRepository.saveAll(tripBags);

        // NEW LOGIC: If all bags for the trip are weighed, update Trip status to 'weighed'
        if (!tripBags.isEmpty()) {
            Long tripId = tripBags.get(0).getTripSupplier().getTrip().getTripId();
            List<TripBag> allTripBags = tripBagRepository.findByTripSupplier_Trip_TripId(tripId);
            boolean allWeighed = allTripBags.stream().allMatch(bag -> "weighed".equalsIgnoreCase(bag.getStatus()));
            if (allWeighed) {
                Trip trip = tripRepository.findById(tripId).orElse(null);
                if (trip != null) {
                    trip.setStatus("weighed");
                    tripRepository.save(trip);
                }
            }
        }
    }

    private void updateSupplyRequestStatusIfAllBagsWeighed(Long supplyRequestId) {
        List<TripBag> allBags = tripBagRepository.findByTripSupplier_TeaSupplyRequest_RequestId(supplyRequestId);
        boolean allWeighed = allBags.stream().allMatch(bag -> "weighed".equalsIgnoreCase(bag.getStatus()));
        if (allWeighed) {
            teaSupplyRequestRepository.findById(supplyRequestId).ifPresent(req -> {
                req.setStatus("weighed");
                teaSupplyRequestRepository.save(req);
            });
        }
    }

    public BagWeight updateBagWeight(Long id, BagWeightDTO dto) {
        BagWeight bagWeight = bagWeightRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("BagWeight not found"));
        bagWeight.setGrossWeight(bagWeight.getGrossWeight() + dto.getGrossWeight());
        bagWeight.setWater(bagWeight.getWater() + dto.getWater());
        bagWeight.setCoarse(bagWeight.getCoarse() + dto.getCoarse());
        bagWeight.setOtherWeight(bagWeight.getOtherWeight() + dto.getOtherWeight());
        bagWeight.setRecordedAt(LocalDateTime.now());
        bagWeight.setDate(LocalDate.now()); // Set date to API call date
        bagWeight.setReason(dto.getReason());
        if (dto.getBagNumbers() != null) {
            bagWeight.setBagTotal(bagWeight.getBagTotal() + dto.getBagNumbers().size());
        }
        // Optionally update other fields if needed
        BagWeight saved = bagWeightRepository.save(bagWeight);
        updateTripBagStatusToWeighed(dto.getSupplyRequestId(), dto.getBagNumbers());
        updateSupplyRequestStatusIfAllBagsWeighed(dto.getSupplyRequestId());
        return saved;
    }
}
