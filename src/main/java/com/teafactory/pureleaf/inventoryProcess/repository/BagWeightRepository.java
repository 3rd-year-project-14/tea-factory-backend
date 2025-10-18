package com.teafactory.pureleaf.inventoryProcess.repository;

import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventorySummaryDto;
import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventoryRouteSummaryDTO;
import com.teafactory.pureleaf.inventoryProcess.entity.BagWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BagWeightRepository extends JpaRepository<BagWeight, Long>, JpaSpecificationExecutor<BagWeight> {
    List<BagWeight> findBySupplyRequest_RequestIdAndDate(Long supplyRequestId, LocalDate date);

    List<BagWeight> findByWeighingSession_SessionId(Long sessionId);

    // Projection for trip gross weight aggregation
    interface TripGrossWeightProjection {
        Long getTripId();

        Double getTotalGrossWeight();
    }

    // Sum grossWeight grouped by trip ids (across all sessions of each trip)
    @Query("SELECT b.weighingSession.trip.tripId AS tripId, SUM(b.grossWeight) AS totalGrossWeight " +
            "FROM BagWeight b WHERE b.weighingSession.trip.tripId IN :tripIds GROUP BY b.weighingSession.trip.tripId")
    List<TripGrossWeightProjection> sumGrossWeightByTripIds(@Param("tripIds") List<Long> tripIds);

    // Projection for trip tare weight aggregation
    interface TripTareWeightProjection {
        Long getTripId();

        Double getTotalTareWeight();
    }

    // Sum tareWeight grouped by trip ids (across all sessions of each trip)
    @Query("SELECT b.weighingSession.trip.tripId AS tripId, SUM(b.tareWeight) AS totalTareWeight " +
            "FROM BagWeight b WHERE b.weighingSession.trip.tripId IN :tripIds GROUP BY b.weighingSession.trip.tripId")
    List<TripTareWeightProjection> sumTareWeightByTripIds(@Param("tripIds") List<Long> tripIds);

    // Today's weighing summary filtered by trip and session status (case-insensitive), using sessionDate
    interface TodayWeighingSummaryProjection {
        Long getTotalSuppliers();

        Long getTotalBags();

        Double getTotalGrossWeight();
    }

    @Query("SELECT COUNT(DISTINCT b.supplyRequest.supplier.supplierId) AS totalSuppliers, " +
            "COALESCE(SUM(b.bagTotal), 0) AS totalBags, " +
            "COALESCE(SUM(b.grossWeight), 0) AS totalGrossWeight " +
            "FROM BagWeight b " +
            "WHERE b.weighingSession.trip.tripId = :tripId " +
            "AND LOWER(b.weighingSession.status) = LOWER(:status) " +
            "AND b.weighingSession.sessionDate = :today")
    TodayWeighingSummaryProjection getTodayWeighingSummaryByTripAndStatus(@Param("tripId") Long tripId,
                                                                          @Param("status") String status,
                                                                          @Param("today") LocalDate today);

    // Dashboard projection for total bags and gross weight for a factory on a given date
    interface FactoryBagWeightSummaryProjection {
        Long getTotalBags();

        Double getTotalGrossWeight();
    }

    @Query("SELECT COALESCE(SUM(b.bagTotal), 0) AS totalBags, COALESCE(SUM(b.grossWeight), 0) AS totalGrossWeight " +
            "FROM BagWeight b WHERE b.weighingSession.trip.route.factory.factoryId = :factoryId AND b.date = :today")
    FactoryBagWeightSummaryProjection getFactoryBagWeightSummary(@Param("factoryId") Long factoryId, @Param("today") java.time.LocalDate today);

    // Inventory summary for daily view using DTO projection
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventorySummaryDto(" +
            "COALESCE(SUM(b.grossWeight),0), COALESCE(SUM(b.netWeight),0), COALESCE(SUM(b.bagTotal),0)) " +
            "FROM BagWeight b WHERE b.weighingSession.trip.route.factory.factoryId = :factoryId AND b.date = :date")
    InventorySummaryDto getInventorySummaryByFactoryAndDate(@Param("factoryId") Long factoryId, @Param("date") LocalDate date);

    // Inventory summary for monthly view using DTO projection (PostgreSQL compatible)
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventorySummaryDto(" +
            "COALESCE(SUM(b.grossWeight),0), COALESCE(SUM(b.netWeight),0), COALESCE(SUM(b.bagTotal),0)) " +
            "FROM BagWeight b WHERE b.weighingSession.trip.route.factory.factoryId = :factoryId " +
            "AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year")
    InventorySummaryDto getInventorySummaryByFactoryAndMonth(@Param("factoryId") Long factoryId, @Param("month") int month, @Param("year") int year);

    // Daily view
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventoryRouteSummaryDTO(" +
            "r.routeId, r.name, r.routeCode, COUNT(DISTINCT b.supplyRequest.supplier.supplierId) as supplierCount, " +
            "COALESCE(SUM(b.grossWeight),0) as totalGrossWeight, " +
            "COALESCE(SUM(b.netWeight),0) as netWeight) " +
            "FROM BagWeight b " +
            "JOIN b.weighingSession ws " +
            "JOIN ws.trip t " +
            "JOIN t.route r " +
            "WHERE r.factory.factoryId = :factoryId AND b.date = :date " +
            "AND (:search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY r.routeId, r.name, r.routeCode")
    Page<InventoryRouteSummaryDTO> getRouteInventorySummaryByFactoryAndDate(
            @Param("factoryId") Long factoryId,
            @Param("date") LocalDate date,
            @Param("search") String search,
            Pageable pageable
    );

    // Monthly view
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventoryRouteSummaryDTO(" +
            "r.routeId, r.name, r.routeCode, COUNT(DISTINCT b.supplyRequest.supplier.supplierId) as supplierCount, " +
            "COALESCE(SUM(b.grossWeight),0) as totalGrossWeight, " +
            "COALESCE(SUM(b.netWeight),0) as netWeight) " +
            "FROM BagWeight b " +
            "JOIN b.weighingSession ws " +
            "JOIN ws.trip t " +
            "JOIN t.route r " +
            "WHERE r.factory.factoryId = :factoryId " +
            "AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year " +
            "AND (:search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY r.routeId, r.name, r.routeCode")
    Page<InventoryRouteSummaryDTO> getRouteInventorySummaryByFactoryAndMonth(
            @Param("factoryId") Long factoryId,
            @Param("month") int month,
            @Param("year") int year,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(b.netWeight), 0) FROM BagWeight b WHERE b.supplyRequest.supplier.supplierId = :supplierId AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year")
    BigDecimal findTotalNetWeightBySupplierIdAndMonth(@Param("supplierId") Long supplierId, @Param("month") int month, @Param("year") int year);
}
