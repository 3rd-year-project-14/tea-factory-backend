package com.teafactory.pureleaf.driverProcess.repository;

import com.teafactory.pureleaf.driverProcess.entity.DriverAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverAvailabilityRepository extends JpaRepository<DriverAvailability, Long> {
    List<DriverAvailability> findByDriver_DriverId(Long driverId);
    DriverAvailability findByDriver_DriverIdAndDate(Long driverId, java.time.LocalDate date);
}
