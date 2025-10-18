package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.VehicleDTO;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.Vehicle;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private FactoryRepository factoryRepository;

    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<VehicleDTO> getVehicleById(String vehicleNo) {
        return vehicleRepository.findById(vehicleNo)
                .map(this::convertToDTO);
    }

    // Create vehicle using factoryId provided in DTO
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNo(vehicleDTO.getVehicleNo());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setCapacity(vehicleDTO.getCapacity());
        vehicle.setStatus("Available");
        vehicle.setRegisteredDate(LocalDate.now().atStartOfDay());
        vehicle.setIncomeCertificate(vehicleDTO.getIncomeCertificate());
        vehicle.setImage(vehicleDTO.getImage());
        if (vehicleDTO.getFactoryId() != null) {
            factoryRepository.findById(vehicleDTO.getFactoryId()).ifPresent(vehicle::setFactory);
        }
        Vehicle saved = vehicleRepository.save(vehicle);
        return convertToDTO(saved);
    }

    public Optional<VehicleDTO> updateVehicle(String vehicleNo, VehicleDTO vehicleDTO) {
        return vehicleRepository.findById(vehicleNo).map(existing -> {
            existing.setModel(vehicleDTO.getModel());
            existing.setCapacity(vehicleDTO.getCapacity());
            // do not change status or registeredDate on update unless needed
            existing.setIncomeCertificate(vehicleDTO.getIncomeCertificate());
            existing.setImage(vehicleDTO.getImage());
            // update factory if factoryId provided
            if (vehicleDTO.getFactoryId() != null) {
                factoryRepository.findById(vehicleDTO.getFactoryId()).ifPresent(existing::setFactory);
            }
            Vehicle saved = vehicleRepository.save(existing);
            return convertToDTO(saved);
        });
    }

    public void deleteVehicle(String vehicleNo) {
        vehicleRepository.deleteById(vehicleNo);
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleNo(vehicle.getVehicleNo());
        dto.setModel(vehicle.getModel());
        dto.setCapacity(vehicle.getCapacity());
        dto.setIncomeCertificate(vehicle.getIncomeCertificate());
        dto.setImage(vehicle.getImage());
        if (vehicle.getFactory() != null) dto.setFactoryId(vehicle.getFactory().getFactoryId());
        return dto;
    }
}
