package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.DriverDTO;
import com.teafactory.pureleaf.entity.Driver;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.Route;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.repository.DriverRepository;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.repository.RouteRepository;
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
    private RouteRepository routeRepository;
    @Autowired
    private UserRepository userRepository;

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
        driver.setLicenseExpiry(driverDTO.getLicenseExpiry());
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
        if (driverDTO.getRouteId() != null) {
            Route route = routeRepository.findById(driverDTO.getRouteId())
                    .orElseThrow(() -> new RuntimeException("Route not found"));
            driver.setRoute(route);
        }
        if (driverDTO.getUserId() != null) {
            User user = userRepository.findById(driverDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            driver.setUser(user);
        }

        Driver savedDriver = driverRepository.save(driver);
        return convertToDTO(savedDriver);
    }

    private DriverDTO convertToDTO(Driver driver) {
        return new DriverDTO(
                driver.getDriverId(),
                driver.getDriverType(),
                driver.getLicenseImage(),
                driver.getLicenseStatus(),
                driver.getLicenseExpiry(),
                driver.getVehicleNo(),
                driver.getVehicleCapacity(),
                driver.getEmergencyContact(),
                driver.getIsActive(),
                driver.getCreatedAt(),
                driver.getFactory() != null ? driver.getFactory().getFactoryId() : null,
                driver.getRoute() != null ? driver.getRoute().getRouteId() : null,
                driver.getUser() != null ? driver.getUser().getId() : null
        );
    }
}
