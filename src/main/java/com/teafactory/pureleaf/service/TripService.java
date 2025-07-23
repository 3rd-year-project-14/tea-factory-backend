package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.TripDTO;
import com.teafactory.pureleaf.entity.Trip;
import com.teafactory.pureleaf.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    public List<TripDTO> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TripDTO> getTripById(Long id) {
        return tripRepository.findById(id)
                .map(this::convertToDTO);
    }

    private TripDTO convertToDTO(Trip trip) {
        return new TripDTO(
                trip.getTripId(),
                trip.getDriver() != null ? trip.getDriver().getDriverId() : null,
                trip.getTripDate(),
                trip.getStartTime(),
                trip.getEndTime()
        );
    }
}

