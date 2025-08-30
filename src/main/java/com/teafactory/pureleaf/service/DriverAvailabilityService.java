package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.DriverAvailabilityDTO;
import com.teafactory.pureleaf.entity.Driver;
import com.teafactory.pureleaf.entity.DriverAvailability;
import com.teafactory.pureleaf.repository.DriverAvailabilityRepository;
import com.teafactory.pureleaf.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverAvailabilityService {
    @Autowired
    private DriverAvailabilityRepository driverAvailabilityRepository;

    @Autowired
    private DriverRepository driverRepository;

    public List<DriverAvailabilityDTO> getAvailabilityByDriverId(Long driverId) {
        List<DriverAvailability> availabilities = driverAvailabilityRepository.findByDriver_DriverId(driverId);
        return availabilities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DriverAvailabilityDTO convertToDTO(DriverAvailability availability) {
        return new DriverAvailabilityDTO(
                availability.getId(),
                availability.getDriver().getDriverId(),
                availability.getDate(),
                availability.getIsAvailable(),
                availability.getReason()
        );
    }

    public DriverAvailabilityDTO createAvailability(DriverAvailabilityDTO dto) {
        if (dto.getIsAvailable() == null) {
            throw new IllegalArgumentException("isAvailable must be provided");
        }
        if (!dto.getIsAvailable() && (dto.getReason() == null || dto.getReason().isEmpty())) {
            throw new IllegalArgumentException("Reason must be provided if not available");
        }
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        java.time.LocalDate today = java.time.LocalDate.now();
        // Check if an availability record already exists for this driver and today
        List<DriverAvailability> existing = driverAvailabilityRepository.findByDriver_DriverId(driver.getDriverId())
            .stream().filter(a -> today.equals(a.getDate())).toList();
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("Availability for today already exists for this driver");
        }
        DriverAvailability availability = new DriverAvailability();
        availability.setDriver(driver);
        availability.setDate(today);
        availability.setIsAvailable(dto.getIsAvailable());
        availability.setReason(dto.getReason());
        DriverAvailability saved = driverAvailabilityRepository.save(availability);
        return convertToDTO(saved);
    }

    public DriverAvailabilityDTO updateAvailability(Long id, DriverAvailabilityDTO dto) {
        DriverAvailability availability = driverAvailabilityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Driver availability not found"));
        if (dto.getIsAvailable() == null) {
            throw new IllegalArgumentException("isAvailable must be provided");
        }
        Boolean previousAvailable = availability.getIsAvailable();
        Boolean newAvailable = dto.getIsAvailable();
        if (previousAvailable != null && previousAvailable && !newAvailable) {
            // true -> false, must provide reason
            if (dto.getReason() == null || dto.getReason().isEmpty()) {
                throw new IllegalArgumentException("Reason must be provided if not available");
            }
            availability.setReason(dto.getReason());
        } else if (previousAvailable != null && !previousAvailable && newAvailable) {
            // false -> true, clear reason
            availability.setReason(null);
        } // else, keep reason as is or update if provided
        availability.setIsAvailable(newAvailable);
        if (dto.getDate() != null) {
            availability.setDate(dto.getDate());
        }
        DriverAvailability saved = driverAvailabilityRepository.save(availability);
        return convertToDTO(saved);
    }

    public DriverAvailabilityDTO getTodayAvailability(Long driverId) {
        java.time.LocalDate today = java.time.LocalDate.now();
        DriverAvailability availability = driverAvailabilityRepository.findByDriver_DriverIdAndDate(driverId, today);
        return availability != null ? convertToDTO(availability) : null;
    }
}
