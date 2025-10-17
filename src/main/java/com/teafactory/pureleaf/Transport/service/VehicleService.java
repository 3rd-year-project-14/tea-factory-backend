package com.teafactory.pureleaf.Transport.service;

import com.teafactory.pureleaf.Transport.dto.VehicleDTO;
import com.teafactory.pureleaf.Transport.entity.Vehicle;
import com.teafactory.pureleaf.Transport.repository.VehicleRepository;
import com.teafactory.pureleaf.driverProcess.entity.Driver;
import com.teafactory.pureleaf.driverProcess.repository.DriverRepository;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    public VehicleDTO createVehicle(VehicleDTO dto) {
        if (dto.getVehicleNumber() != null && vehicleRepository.findByVehicleNumber(dto.getVehicleNumber()) != null) {
            throw new IllegalArgumentException("Vehicle with number already exists: " + dto.getVehicleNumber());
        }
        Vehicle v = toEntity(dto);
        Vehicle saved = vehicleRepository.save(v);
        return toDto(saved);
    }

    public VehicleDTO getVehicleById(Long id) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return toDto(v);
    }

    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO dto) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        if (dto.getVehicleNumber() != null) v.setVehicleNumber(dto.getVehicleNumber());
        if (dto.getVehicleType() != null) v.setVehicleType(dto.getVehicleType());
        if (dto.getCapacity() != null) v.setCapacity(dto.getCapacity());
        if (dto.getStatus() != null) v.setStatus(dto.getStatus());
        if (dto.getLastServiceDate() != null) v.setLastServiceDate(dto.getLastServiceDate());
        if (dto.getVehicleImage() != null) v.setVehicleImage(dto.getVehicleImage());

        if (dto.getAssignedDriverId() != null) {
            Driver d = driverRepository.findById(dto.getAssignedDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + dto.getAssignedDriverId()));
            v.setAssignedDriver(d);
        } else {
            v.setAssignedDriver(null);
        }

        Vehicle saved = vehicleRepository.save(v);
        return toDto(saved);
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    private Vehicle toEntity(VehicleDTO dto) {
        Vehicle v = new Vehicle();
        v.setVehicleNumber(dto.getVehicleNumber());
        v.setVehicleType(dto.getVehicleType());
        v.setCapacity(dto.getCapacity());
        v.setStatus(dto.getStatus());
        v.setLastServiceDate(dto.getLastServiceDate());
        v.setVehicleImage(dto.getVehicleImage());
        if (dto.getAssignedDriverId() != null) {
            Optional<Driver> d = driverRepository.findById(dto.getAssignedDriverId());
            if (d.isPresent()) v.setAssignedDriver(d.get());
            else throw new ResourceNotFoundException("Driver not found with id: " + dto.getAssignedDriverId());
        }
        return v;
    }

    private VehicleDTO toDto(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleId(v.getVehicleId());
        dto.setVehicleNumber(v.getVehicleNumber());
        dto.setVehicleType(v.getVehicleType());
        dto.setCapacity(v.getCapacity());
        dto.setStatus(v.getStatus());
        dto.setLastServiceDate(v.getLastServiceDate());
        dto.setVehicleImage(v.getVehicleImage());
        dto.setCreatedAt(v.getCreatedAt());
        if (v.getAssignedDriver() != null) {
            dto.setAssignedDriverId(v.getAssignedDriver().getDriverId());
            if (v.getAssignedDriver().getUser() != null) dto.setAssignedDriverName(v.getAssignedDriver().getUser().getName());
        }
        return dto;
    }
}

