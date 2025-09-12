package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.inventoryProcess.dto.BagWeightDTO;
import com.teafactory.pureleaf.inventoryProcess.entity.BagWeight;
import com.teafactory.pureleaf.inventoryProcess.entity.TeaSupplyRequest;
import com.teafactory.pureleaf.inventoryProcess.entity.Trip;
import com.teafactory.pureleaf.inventoryProcess.entity.TripBag;
import com.teafactory.pureleaf.inventoryProcess.entity.WeighingSession;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.TeaSupplyRequestRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.TripBagRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.TripRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.WeighingSessionRepository;
import com.teafactory.pureleaf.inventoryProcess.dto.WeighedBagWeightDetailsDTO;
import com.teafactory.pureleaf.inventoryProcess.spec.BagWeightSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BagWeightService {

    private final BagWeightRepository bagWeightRepository;
    private final TeaSupplyRequestRepository teaSupplyRequestRepository;
    private final WeighingSessionRepository weighingSessionRepository;
    private final TripBagRepository tripBagRepository;
    private final TripRepository tripRepository;

    public BagWeightService(BagWeightRepository bagWeightRepository, TeaSupplyRequestRepository teaSupplyRequestRepository, WeighingSessionRepository weighingSessionRepository, TripBagRepository tripBagRepository, TripRepository tripRepository) {
        this.bagWeightRepository = bagWeightRepository;
        this.teaSupplyRequestRepository = teaSupplyRequestRepository;
        this.weighingSessionRepository = weighingSessionRepository;
        this.tripBagRepository = tripBagRepository;
        this.tripRepository = tripRepository;
    }

    public List<BagWeight> getAllBagWeights() {
        return bagWeightRepository.findAll();
    }

    public Optional<BagWeight> getBagWeightById(Long id) {
        return bagWeightRepository.findById(id);
    }

    @Transactional
    public BagWeight createBagWeight(BagWeightDTO dto) {
        TeaSupplyRequest supplyRequest = teaSupplyRequestRepository.findById(dto.getSupplyRequestId())
                .orElseThrow(() -> new RuntimeException("SupplyRequest not found"));
        WeighingSession weighingSession = weighingSessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("WeighingSession not found"));
        System.out.println( "weighingSession id: " + weighingSession.getSessionId());

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
            // Update WeighingSession status
            List<WeighingSession> sessions = weighingSessionRepository.findByTrip_TripId(tripId);
            for (WeighingSession session : sessions) {
                session.setStatus("weighed");
            }
            weighingSessionRepository.saveAll(sessions);
            // Update Trip status
            Trip trip = weighingSession.getTrip();
            trip.setStatus("weighed");
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

    public com.teafactory.pureleaf.inventoryProcess.dto.BagWeightResponseDTO mapToResponseDTO(BagWeight bagWeight) {
        com.teafactory.pureleaf.inventoryProcess.dto.BagWeightResponseDTO dto = new com.teafactory.pureleaf.inventoryProcess.dto.BagWeightResponseDTO();
        dto.setId(bagWeight.getId());
        dto.setCoarse(bagWeight.getCoarse());
        dto.setGrossWeight(bagWeight.getGrossWeight());
        dto.setNetWeight(bagWeight.getNetWeight());
        dto.setRecordedAt(bagWeight.getRecordedAt());
        dto.setTareWeight(bagWeight.getTareWeight());
        dto.setOtherWeight(bagWeight.getOtherWeight());
        dto.setReason(bagWeight.getReason());
        dto.setBagTotal(bagWeight.getBagTotal());
        return dto;
    }

    public Page<WeighedBagWeightDetailsDTO> getBagWeightDetailsBySessionIdPaged(Long sessionId, String search, String status, Pageable pageable) {
        Specification<com.teafactory.pureleaf.inventoryProcess.entity.BagWeight> spec = Specification
            .where(BagWeightSpecs.hasSessionId(sessionId))
            .and(BagWeightSpecs.searchSupplier(search))
            .and(BagWeightSpecs.hasStatus(status));
        Page<com.teafactory.pureleaf.inventoryProcess.entity.BagWeight> page = bagWeightRepository.findAll(spec, pageable);
        return page.map(bagWeight -> {
            Long supplierId = null;
            String supplierName = null;
            String statusVal = null;
            if (bagWeight.getSupplyRequest() != null && bagWeight.getSupplyRequest().getSupplier() != null) {
                supplierId = bagWeight.getSupplyRequest().getSupplier().getSupplierId();
                if (bagWeight.getSupplyRequest().getSupplier().getUser() != null) {
                    supplierName = bagWeight.getSupplyRequest().getSupplier().getUser().getName();
                }
            }
            if (bagWeight.getSupplyRequest() != null) {
                statusVal = bagWeight.getSupplyRequest().getStatus();
            }
            return new WeighedBagWeightDetailsDTO(
                bagWeight.getId(),
                bagWeight.getBagTotal(),
                (int) bagWeight.getCoarse(),
                bagWeight.getGrossWeight(),
                bagWeight.getTareWeight(),
                supplierId,
                supplierName,
                statusVal
            );
        });
    }
}
