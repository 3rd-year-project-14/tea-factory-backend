package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.TripSupplierDTO;
import com.teafactory.pureleaf.entity.TripSupplier;
import com.teafactory.pureleaf.repository.TripSupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripSupplierService {
    @Autowired
    private TripSupplierRepository tripSupplierRepository;

    public List<TripSupplierDTO> getSuppliersByTripId(Long tripId) {
        List<TripSupplier> tripSuppliers = tripSupplierRepository.findByTrip_TripId(tripId);
        return tripSuppliers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TripSupplierDTO> getAllTripSuppliers() {
        List<TripSupplier> all = tripSupplierRepository.findAll();
        return all.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TripSupplierDTO convertToDTO(TripSupplier ts) {
        return new TripSupplierDTO(
                ts.getTrip().getTripId(),
                ts.getTeaSupplyRequest().getRequestId(),
                ts.getArrivedTime(),
                ts.getCompletedTime(),
                ts.getStatus(),
                ts.getDate()
        );
    }
}
