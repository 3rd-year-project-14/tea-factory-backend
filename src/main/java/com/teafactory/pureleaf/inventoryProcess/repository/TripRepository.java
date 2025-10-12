package com.teafactory.pureleaf.inventoryProcess.repository;

import com.teafactory.pureleaf.inventoryProcess.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
    Optional<Trip> findByDriver_DriverIdAndTripDate(Long driverId, LocalDate tripDate);


    // Projection for aggregated counts
    interface TripStatusAggregation {
        long getPendingCount();
        long getArrivedCount();
        long getWeighedCount();
        long getCompletedCount();
    }

    // New projection for trip summaries
    interface TripSummaryProjection {
        Long getTripId();
        Long getRouteId();
        String getRouteName();
        String getDriverName();
        long getBagCount();
    }

    // Projection for TodayTripDetailsResponse
    interface TodayTripDetailsProjection {
        Long getTripId();
        String getRouteName();
        String getRouteCode();
        String getDriverName();
        Integer getTotalBags();
        Double getTotalWeight();
        Integer getTotalSuppliers();
        Integer getCompletedSuppliers();
        String getTripStatus();
        LocalTime getLastUpdate();
    }

    // Single aggregated query to reduce data transfer and Java-side processing
    @Query("SELECT " +
            "COALESCE(SUM(CASE WHEN LOWER(t.status) IN ('pending','collected') THEN 1 ELSE 0 END),0) AS pendingCount, " +
            "COALESCE(SUM(CASE WHEN LOWER(t.status) = 'arrived' THEN 1 ELSE 0 END),0) AS arrivedCount, " +
            "COALESCE(SUM(CASE WHEN LOWER(t.status) = 'weighed' THEN 1 ELSE 0 END),0) AS weighedCount, " +
            "COALESCE(SUM(CASE WHEN LOWER(t.status) IN ('completed','complete') THEN 1 ELSE 0 END),0) AS completedCount " +
            "FROM Trip t WHERE t.route.factory.factoryId = :factoryId AND t.tripDate = :tripDate")
    TripStatusAggregation aggregateStatusCounts(@Param("factoryId") Long factoryId, @Param("tripDate") LocalDate tripDate);

    // New paginated + searchable summaries
    @Query(value = "SELECT t.tripId AS tripId, r.routeId AS routeId, r.name AS routeName, u.name AS driverName, COUNT(tb.id) AS bagCount " +
            "FROM Trip t " +
            "LEFT JOIN t.route r " +
            "LEFT JOIN t.driver d " +
            "LEFT JOIN d.user u " +
            "LEFT JOIN TripSupplier ts ON ts.trip = t " +
            "LEFT JOIN TripBag tb ON tb.tripSupplier = ts " +
            "WHERE r.factory.factoryId = :factoryId " +
            "AND t.tripDate = :tripDate " +
            "AND ((LOWER(:status) = 'pending' AND LOWER(t.status) IN ('pending','collected')) OR (LOWER(:status) <> 'pending' AND LOWER(t.status) = LOWER(:status))) " +
            "AND (:search IS NULL OR :search = '' OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR CONCAT(r.routeId, '') LIKE CONCAT('%', :search, '%')) " +
            "GROUP BY t.tripId, r.routeId, r.name, u.name",
            countQuery = "SELECT COUNT(DISTINCT t.tripId) FROM Trip t " +
                    "LEFT JOIN t.route r " +
                    "LEFT JOIN t.driver d " +
                    "LEFT JOIN d.user u " +
                    "WHERE r.factory.factoryId = :factoryId " +
                    "AND t.tripDate = :tripDate " +
                    "AND ((LOWER(:status) = 'pending' AND LOWER(t.status) IN ('pending','collected')) OR (LOWER(:status) <> 'pending' AND LOWER(t.status) = LOWER(:status))) " +
                    "AND (:search IS NULL OR :search = '' OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR CONCAT(r.routeId, '') LIKE CONCAT('%', :search, '%'))")
    Page<TripSummaryProjection> findTripSummaries(@Param("factoryId") Long factoryId,
                                                  @Param("status") String status,
                                                  @Param("tripDate") LocalDate tripDate,
                                                  @Param("search") String search,
                                                  Pageable pageable);

    // Custom query for today's trip details with pagination
    @Query(value = "SELECT t.tripId AS tripId, " +
            "r.name AS routeName, " +
            "r.routeCode AS routeCode, " +
            "u.name AS driverName, " +
            "COUNT(DISTINCT tb.id) AS totalBags, " +
            "COALESCE(SUM(tb.driverWeight), 0) AS totalWeight, " +
            "(SELECT COUNT(tsr.requestId) FROM TeaSupplyRequest tsr WHERE tsr.supplier.route.routeId = t.route.routeId AND tsr.supplyDate = :tripDate) AS totalSuppliers, " +
            "(SELECT COUNT(ts2.id.requestId) FROM TripSupplier ts2 WHERE ts2.trip.tripId = t.tripId AND ts2.status = 'completed') AS completedSuppliers, " +
            "t.status AS tripStatus, " +
            "(SELECT MAX(ts2.completionTime) FROM TripSupplier ts2 WHERE ts2.trip.tripId = t.tripId AND ts2.status = 'completed') AS lastUpdate " +
            "FROM Trip t " +
            "JOIN t.route r " +
            "JOIN t.driver d " +
            "JOIN d.user u " +
            "LEFT JOIN TripSupplier tsp ON tsp.trip = t " +
            "LEFT JOIN TripBag tb ON tb.tripSupplier = tsp " +
            "WHERE r.factory.factoryId = :factoryId AND t.tripDate = :tripDate " +
            "AND (:search IS NULL OR :search = '' OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY t.tripId, r.name, r.routeCode, u.name, t.status " +
            "ORDER BY lastUpdate DESC, t.tripId DESC",
            countQuery = "SELECT COUNT(DISTINCT t.tripId) FROM Trip t WHERE t.route.factory.factoryId = :factoryId AND t.tripDate = :tripDate AND (:search IS NULL OR :search = '' OR LOWER(t.route.name) LIKE LOWER(CONCAT('%', :search, '%')))" )
    Page<TodayTripDetailsProjection> findTodayTripDetailsByFactoryIdAndTripDate(@Param("factoryId") Long factoryId, @Param("tripDate") java.time.LocalDate tripDate, @Param("search") String search, Pageable pageable);
}
