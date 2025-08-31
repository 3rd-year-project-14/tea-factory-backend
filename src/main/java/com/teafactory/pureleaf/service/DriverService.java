package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.DriverDTO;
import com.teafactory.pureleaf.entity.Driver;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.repository.DriverRepository;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RouteRepository routeRepository;

    public List<DriverDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<DriverDTO> getDriverById(Long id) {
        return driverRepository.findById(id)
                .map(this::convertToDTO);
    }

    public DriverDTO createDriver(DriverDTO driverDTO) {
        Driver driver = new Driver();
        driver.setDriverType(driverDTO.getDriverType());
        driver.setLicenseImage(driverDTO.getLicenseImage());
        driver.setLicenseStatus(driverDTO.getLicenseStatus());
        driver.setLicenseExpiry(driverDTO.getLicenseExpiry() != null ? driverDTO.getLicenseExpiry().atStartOfDay() : null);
        driver.setVehicleNo(driverDTO.getVehicleNo());
        driver.setVehicleCapacity(driverDTO.getVehicleCapacity());
        driver.setEmergencyContact(driverDTO.getEmergencyContact());
        driver.setIsActive(driverDTO.getIsActive());
        if (driverDTO.getCreatedAt() != null) {
            driver.setCreatedAt(driverDTO.getCreatedAt());
        } else {
            driver.setCreatedAt(java.time.LocalDateTime.now());
        }

        if (driverDTO.getFactoryId() != null) {
            Factory factory = factoryRepository.findById(driverDTO.getFactoryId())
                    .orElseThrow(() -> new RuntimeException("Factory not found"));
            driver.setFactory(factory);
        }
        if (driverDTO.getUserId() != null) {
            User user = userRepository.findById(driverDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            driver.setUser(user);
        }

        Driver savedDriver = driverRepository.save(driver);
        return convertToDTO(savedDriver);
    }

    public DriverDTO getDriverDetailsByUserId(Long userId) {
        Driver driver = driverRepository.findByUser_Id(userId);
        if (driver == null) return null;
        DriverDTO dto = convertToDTO(driver);
        // Only set route details if needed
        Route route = routeRepository.findFirstByDriver_DriverId(driver.getDriverId());
        if (route != null) {
            dto.setRouteId(route.getRouteId());
            dto.setRouteName(route.getName());
            dto.setRouteStartLocation(route.getStartLocation());
            dto.setRouteEndLocation(route.getEndLocation());
        }
        // Set factory name and mapUrl if factory exists
        Factory factory = driver.getFactory();
        if (factory != null) {
            dto.setFactoryName(factory.getName());
            dto.setFactoryMapUrl(factory.getMapUrl());
        }
        return dto;
    }

    private DriverDTO convertToDTO(Driver driver) {
        DriverDTO dto = new DriverDTO();
        dto.setDriverId(driver.getDriverId());
        dto.setDriverType(driver.getDriverType());
        dto.setLicenseImage(driver.getLicenseImage());
        dto.setLicenseStatus(driver.getLicenseStatus());
        dto.setLicenseExpiry(driver.getLicenseExpiry() != null ? driver.getLicenseExpiry().toLocalDate() : null);
        dto.setVehicleNo(driver.getVehicleNo());
        dto.setVehicleCapacity(driver.getVehicleCapacity());
        dto.setEmergencyContact(driver.getEmergencyContact());
        dto.setIsActive(driver.getIsActive());
        dto.setCreatedAt(driver.getCreatedAt());
        dto.setFactoryId(driver.getFactory() != null ? driver.getFactory().getFactoryId() : null);
        dto.setUserId(driver.getUser() != null ? driver.getUser().getId() : null);
        // RouteId and route details will be set in getDriverDetailsByUserId if needed
        return dto;
    }
}
