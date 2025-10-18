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
import com.teafactory.pureleaf.driverProcess.dto.DriverRegistrationDTO;
import com.teafactory.pureleaf.auth.entity.Role;
import java.time.LocalDateTime;

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

    // Register a new User (ROLE_DRIVER) and associated Driver in one call
    public DriverDTO registerDriver(DriverRegistrationDTO regDto) {
        // basic uniqueness checks
        if (regDto.getEmail() != null && userRepository.existsByEmail(regDto.getEmail())) {
           throw new RuntimeException("Email is already in use: " + regDto.getEmail());
        }
        if (regDto.getFirebaseUid() != null && userRepository.findByFirebaseUid(regDto.getFirebaseUid()).isPresent()) {
            throw new RuntimeException("User already exists with firebaseUid: " + regDto.getFirebaseUid());
        }
         // create user
         User user = new User();
         user.setFirebaseUid(regDto.getFirebaseUid());
         user.setEmail(regDto.getEmail());
         user.setName(regDto.getName());
         user.setNic(regDto.getNic());
         user.setContactNo(regDto.getContactNo());
         user.setAddress(regDto.getAddress());
         user.setRole(Role.DRIVER);
         user.setCreatedAt(LocalDateTime.now());
         user.setUpdatedAt(LocalDateTime.now());

         if (regDto.getFactoryId() != null) {
             Factory factory = factoryRepository.findById(regDto.getFactoryId())
                     .orElseThrow(() -> new ResourceNotFoundException("Factory not found with id: " + regDto.getFactoryId()));
             user.setFactory(factory);
         }

         User savedUser = userRepository.save(user);

         // create driver
         Driver driver = new Driver();
         driver.setDriverType(regDto.getDriverType());
         driver.setLicenseImage(regDto.getLicenseImage());
         driver.setLicenseStatus(regDto.getLicenseStatus());
         driver.setLicenseExpiry(regDto.getLicenseExpiry());
         driver.setVehicleNo(regDto.getVehicleNo());
         driver.setVehicleCapacity(regDto.getVehicleCapacity());
         driver.setEmergencyContact(regDto.getEmergencyContact());
         driver.setIsActive(regDto.getIsActive());
         driver.setCreatedAt(regDto.getCreatedAt());
         driver.setUser(savedUser);

         if (regDto.getFactoryId() != null) {
             Factory factory = factoryRepository.findById(regDto.getFactoryId())
                     .orElseThrow(() -> new ResourceNotFoundException("Factory not found with id: " + regDto.getFactoryId()));
             driver.setFactory(factory);
         }

         Driver savedDriver = driverRepository.save(driver);
         return driverMapper.toDto(savedDriver);
     }

     public DriverDTO getDriverDetailsByUserId(Long userId) {
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
