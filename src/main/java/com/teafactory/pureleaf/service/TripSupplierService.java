package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.TripSupplierDTO;
import com.teafactory.pureleaf.entity.Trip;
import com.teafactory.pureleaf.entity.TeaSupplyRequest;
import com.teafactory.pureleaf.entity.TripSupplier;
import com.teafactory.pureleaf.repository.TripRepository;
import com.teafactory.pureleaf.repository.TeaSupplyRequestRepository;
import com.teafactory.pureleaf.repository.TripSupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripSupplierService {
    @Autowired
    private TripSupplierRepository tripSupplierRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TeaSupplyRequestRepository teaSupplyRequestRepository;

    public List<TripSupplierDTO> getSuppliersByTripId(Long tripId) {
        List<TripSupplier> tripSuppliers = tripSupplierRepository.findByTrip_TripId(tripId);
        return tripSuppliers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TripSupplierDTO> getAllTripSuppliers() {
        List<TripSupplier> all = tripSupplierRepository.findAll();
        return all.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TripSupplierDTO createTripSupplier(Long tripId, Long supplyRequestId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));
        TeaSupplyRequest supplyRequest = teaSupplyRequestRepository.findById(supplyRequestId).orElseThrow(() -> new RuntimeException("Supply request not found"));
        TripSupplier tripSupplier = new TripSupplier();
        tripSupplier.setTrip(trip);
        tripSupplier.setTeaSupplyRequest(supplyRequest);
        tripSupplier.setArrivedTime(LocalTime.now());
        tripSupplier.setStatus("pending");
        TripSupplier saved = tripSupplierRepository.save(tripSupplier);
        return convertToDTO(saved);
    }

    private TripSupplierDTO convertToDTO(TripSupplier ts) {
        return new TripSupplierDTO(
                ts.getTrip().getTripId(),
                ts.getTeaSupplyRequest().getRequestId(),
                ts.getArrivedTime(),
                ts.getCompletedTime(),
                ts.getStatus()
        );
    }
}
