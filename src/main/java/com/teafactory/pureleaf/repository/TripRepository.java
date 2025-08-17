package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByDriver_DriverIdAndTripDate(Long driverId, LocalDate tripDate);

    List<Trip> findByRoute_Factory_FactoryIdAndTripDate(Long factoryId, LocalDate tripDate);
}
