package com.teafactory.pureleaf.driverProcess.service;

import com.teafactory.pureleaf.driverProcess.dto.DriverDTO;
import com.teafactory.pureleaf.driverProcess.entity.Driver;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.driverProcess.repository.DriverRepository;
import com.teafactory.pureleaf.repository.FactoryRepository;
import com.teafactory.pureleaf.routes.repository.RouteRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import com.teafactory.pureleaf.mappers.DriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Autowired
    private DriverMapper driverMapper;


    public Optional<DriverDTO> getDriverById(Long id) {
        return driverRepository.findById(id)
                .map(driverMapper::toDto);
    }

    public DriverDTO createDriver(DriverDTO driverDTO) {
        Driver driver = driverMapper.toEntity(driverDTO);
        if (driverDTO.getFactoryId() != null) {
            Factory factory = factoryRepository.findById(driverDTO.getFactoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Factory not found with id: " + driverDTO.getFactoryId()));
            driver.setFactory(factory);
        }
        if (driverDTO.getUserId() != null) {
            User user = userRepository.findById(driverDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + driverDTO.getUserId()));
            driver.setUser(user);
        }
        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toDto(savedDriver);
    }

    public DriverDTO getDriverDetailsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.teafactory.pureleaf.exception.ResourceNotFoundException("User not found with id: " + userId));

        Driver driver = driverRepository.findByUser_Id(userId);
        if (driver == null) {
            throw new com.teafactory.pureleaf.exception.ResourceNotFoundException("Driver not found for user id: " + userId);
        }
        DriverDTO dto = driverMapper.toDto(driver);

        Route route = routeRepository.findFirstByDriver_DriverId(driver.getDriverId());
        if (route != null) {
            dto.setRouteId(route.getRouteId());
            dto.setRouteName(route.getName());
            dto.setRouteStartLocation(route.getStartLocation());
            dto.setRouteEndLocation(route.getEndLocation());
        }

        return dto;
    }
}
