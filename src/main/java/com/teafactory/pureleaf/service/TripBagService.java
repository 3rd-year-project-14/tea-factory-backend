package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.TripBagDTO;
import com.teafactory.pureleaf.entity.*;
import com.teafactory.pureleaf.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripBagService {
    @Autowired
    private TripBagRepository tripBagRepository;
    @Autowired
    private TripSupplierRepository tripSupplierRepository;
    @Autowired
    private BagRepository bagRepository;

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
        tripBag.setNote(dto.getNote());

        TripSupplier tripSupplier = tripSupplierRepository.findById(new TripSupplier.TripSupplierId(dto.getTripId(), dto.getSupplyRequestId()))
                .orElseThrow(() -> new RuntimeException("TripSupplier not found"));
        tripBag.setTripSupplier(tripSupplier);

        Bag bag = bagRepository.findById(new Bag.BagId(dto.getBagNumber(), dto.getRouteId()))
                .orElseThrow(() -> new RuntimeException("Bag not found"));
        tripBag.setBag(bag);

        TripBag saved = tripBagRepository.save(tripBag);
        return convertToDTO(saved);
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
                tripBag.getNote()
        );
    }
}

