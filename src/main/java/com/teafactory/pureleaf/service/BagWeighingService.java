package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.BagWeighingDTO;
import com.teafactory.pureleaf.entity.BagWeighing;
import com.teafactory.pureleaf.entity.TripBag;
import com.teafactory.pureleaf.entity.WeighingSession;
import com.teafactory.pureleaf.repository.BagWeighingRepository;
import com.teafactory.pureleaf.repository.TripBagRepository;
import com.teafactory.pureleaf.repository.WeighingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BagWeighingService {
    @Autowired
    private BagWeighingRepository bagWeighingRepository;
    @Autowired
    private TripBagRepository tripBagRepository;
    @Autowired
    private WeighingSessionRepository weighingSessionRepository;

    public List<BagWeighingDTO> getAllBagWeighings() {
        return bagWeighingRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public BagWeighingDTO getBagWeighing(Long id) {
        return bagWeighingRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public BagWeighingDTO createBagWeighing(BagWeighingDTO dto) {
        BagWeighing bagWeighing = new BagWeighing();
        bagWeighing.setGrossWeight(dto.getGrossWeight());
        bagWeighing.setTareWeight(dto.getTareWeight());
        bagWeighing.setNetWeight(dto.getNetWeight());
        bagWeighing.setWet(dto.getWet());
        bagWeighing.setCoarse(dto.getCoarse());
        bagWeighing.setOtherWeight(dto.getOtherWeight());
        bagWeighing.setOtherWeightReason(dto.getOtherWeightReason());
        bagWeighing.setType(dto.getType());
        bagWeighing.setRecordedAt(dto.getRecordedAt());

        TripBag tripBag = tripBagRepository.findById(dto.getTripBagId())
                .orElseThrow(() -> new RuntimeException("TripBag not found"));
        bagWeighing.setTripBag(tripBag);

        WeighingSession session = weighingSessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("WeighingSession not found"));
        bagWeighing.setWeighingSession(session);

        BagWeighing saved = bagWeighingRepository.save(bagWeighing);
        return toDTO(saved);
    }

    private BagWeighingDTO toDTO(BagWeighing bagWeighing) {
        return new BagWeighingDTO(
                bagWeighing.getId(),
                bagWeighing.getTripBag() != null ? bagWeighing.getTripBag().getId() : null,
                bagWeighing.getWeighingSession() != null ? bagWeighing.getWeighingSession().getSessionId() : null,
                bagWeighing.getGrossWeight(),
                bagWeighing.getTareWeight(),
                bagWeighing.getNetWeight(),
                bagWeighing.getWet(),
                bagWeighing.getCoarse(),
                bagWeighing.getOtherWeight(),
                bagWeighing.getOtherWeightReason(),
                bagWeighing.getType(),
                bagWeighing.getRecordedAt()
        );
    }
}

