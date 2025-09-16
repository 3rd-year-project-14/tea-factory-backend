package com.teafactory.pureleaf.routes.repository;

import com.teafactory.pureleaf.routes.dto.RouteDetailsDTO;
import com.teafactory.pureleaf.routes.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Route findFirstByDriver_DriverId(Long driverId);

    @Query("SELECT new com.teafactory.pureleaf.routes.dto.RouteDetailsDTO(r.routeId, r.name, r.routeCode) FROM Route r WHERE r.factory.factoryId = :factoryId AND r.status = true")
    List<RouteDetailsDTO> findRouteDetailsByFactoryId(@Param("factoryId") Long factoryId);

    long countByFactory_FactoryIdAndStatusTrue(Long factoryId);
}