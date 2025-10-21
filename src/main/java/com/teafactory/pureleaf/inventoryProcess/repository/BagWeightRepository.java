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
            "FROM BagWeight b WHERE b.weighingSession.trip.route.factory.factoryId = :factoryId AND b.date = :date " +
            "AND (:routeId IS NULL OR b.weighingSession.trip.route.routeId = :routeId)")
    InventorySummaryDto getInventorySummaryByFactoryAndDate(@Param("factoryId") Long factoryId, @Param("date") LocalDate date, @Param("routeId") Long routeId);

    // Inventory summary for monthly view using DTO projection (PostgreSQL compatible)
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventorySummaryDto(" +
            "COALESCE(SUM(b.grossWeight),0), COALESCE(SUM(b.netWeight),0), COALESCE(SUM(b.bagTotal),0)) " +
            "FROM BagWeight b WHERE b.weighingSession.trip.route.factory.factoryId = :factoryId " +
            "AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year " +
            "AND (:routeId IS NULL OR b.weighingSession.trip.route.routeId = :routeId)")
    InventorySummaryDto getInventorySummaryByFactoryAndMonth(@Param("factoryId") Long factoryId, @Param("month") int month, @Param("year") int year, @Param("routeId") Long routeId);

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
            "AND (:routeId IS NULL OR r.routeId = :routeId) " +
            "GROUP BY r.routeId, r.name, r.routeCode")
    Page<InventoryRouteSummaryDTO> getRouteInventorySummaryByFactoryAndDate(
            @Param("factoryId") Long factoryId,
            @Param("date") LocalDate date,
            @Param("search") String search,
            @Param("routeId") Long routeId,
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
            "AND (:routeId IS NULL OR r.routeId = :routeId) " +
            "GROUP BY r.routeId, r.name, r.routeCode")
    Page<InventoryRouteSummaryDTO> getRouteInventorySummaryByFactoryAndMonth(
            @Param("factoryId") Long factoryId,
            @Param("month") int month,
            @Param("year") int year,
            @Param("search") String search,
            @Param("routeId") Long routeId,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(b.netWeight), 0) FROM BagWeight b WHERE b.supplyRequest.supplier.supplierId = :supplierId AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year")
    BigDecimal findTotalNetWeightBySupplierIdAndMonth(@Param("supplierId") Long supplierId, @Param("month") int month, @Param("year") int year);

    // Daily supplier summary for a specific route
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO(" +
            "s.supplierId, s.user.name, COALESCE(SUM(b.grossWeight),0), COALESCE(SUM(b.bagTotal),0), COALESCE(SUM(b.netWeight),0)) " +
            "FROM BagWeight b " +
            "JOIN b.supplyRequest.supplier s " +
            "JOIN b.weighingSession ws " +
            "JOIN ws.trip t " +
            "JOIN t.route r " +
            "WHERE r.routeId = :routeId AND b.date = :date " +
            "AND (:search IS NULL OR LOWER(s.user.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY s.supplierId, s.user.name")
    Page<com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO> getSupplierSummaryByRouteAndDate(
            @Param("routeId") Long routeId,
            @Param("date") LocalDate date,
            @Param("search") String search,
            Pageable pageable
    );

    // Daily supplier summary for a specific route (sorted by total gross weight DESC)
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO(" +
            "s.supplierId, s.user.name, COALESCE(SUM(b.grossWeight),0), COALESCE(SUM(b.bagTotal),0), COALESCE(SUM(b.netWeight),0)) " +
            "FROM BagWeight b " +
            "JOIN b.supplyRequest.supplier s " +
            "JOIN b.weighingSession ws " +
            "JOIN ws.trip t " +
            "JOIN t.route r " +
            "WHERE r.routeId = :routeId AND b.date = :date " +
            "AND (:search IS NULL OR LOWER(s.user.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY s.supplierId, s.user.name " +
            "ORDER BY COALESCE(SUM(b.grossWeight),0) DESC")
    Page<com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO> getSupplierSummaryByRouteAndDateOrderByGrossDesc(
            @Param("routeId") Long routeId,
            @Param("date") LocalDate date,
            @Param("search") String search,
            Pageable pageable
    );

    // Daily supplier summary for a specific route (sorted by total gross weight ASC)
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO(" +
            "s.supplierId, s.user.name, COALESCE(SUM(b.grossWeight),0), COALESCE(SUM(b.bagTotal),0), COALESCE(SUM(b.netWeight),0)) " +
            "FROM BagWeight b " +
            "JOIN b.supplyRequest.supplier s " +
            "JOIN b.weighingSession ws " +
            "JOIN ws.trip t " +
            "JOIN t.route r " +
            "WHERE r.routeId = :routeId AND b.date = :date " +
            "AND (:search IS NULL OR LOWER(s.user.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY s.supplierId, s.user.name " +
            "ORDER BY COALESCE(SUM(b.grossWeight),0) ASC")
    Page<com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO> getSupplierSummaryByRouteAndDateOrderByGrossAsc(
            @Param("routeId") Long routeId,
            @Param("date") LocalDate date,
            @Param("search") String search,
            Pageable pageable
    );

    // Monthly supplier summary for a specific route (sorted by total gross weight DESC)
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO(" +
            "s.supplierId, s.user.name, COALESCE(SUM(b.grossWeight),0), COALESCE(SUM(b.bagTotal),0), COALESCE(SUM(b.netWeight),0)) " +
            "FROM BagWeight b " +
            "JOIN b.supplyRequest.supplier s " +
            "JOIN b.weighingSession ws " +
            "JOIN ws.trip t " +
            "JOIN t.route r " +
            "WHERE r.routeId = :routeId " +
            "AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year " +
            "AND (:search IS NULL OR LOWER(s.user.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY s.supplierId, s.user.name " +
            "ORDER BY COALESCE(SUM(b.grossWeight),0) DESC")
    Page<com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO> getSupplierSummaryByRouteAndMonthOrderByGrossDesc(
            @Param("routeId") Long routeId,
            @Param("month") int month,
            @Param("year") int year,
            @Param("search") String search,
            Pageable pageable
    );

    // Monthly supplier summary for a specific route (sorted by total gross weight ASC)
    @Query("SELECT new com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO(" +
            "s.supplierId, s.user.name, COALESCE(SUM(b.grossWeight),0), COALESCE(SUM(b.bagTotal),0), COALESCE(SUM(b.netWeight),0)) " +
            "FROM BagWeight b " +
            "JOIN b.supplyRequest.supplier s " +
            "JOIN b.weighingSession ws " +
            "JOIN ws.trip t " +
            "JOIN t.route r " +
            "WHERE r.routeId = :routeId " +
            "AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year " +
            "AND (:search IS NULL OR LOWER(s.user.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY s.supplierId, s.user.name " +
            "ORDER BY COALESCE(SUM(b.grossWeight),0) ASC")
    Page<com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO> getSupplierSummaryByRouteAndMonthOrderByGrossAsc(
            @Param("routeId") Long routeId,
            @Param("month") int month,
            @Param("year") int year,
            @Param("search") String search,
            Pageable pageable
    );

    // Projection for supplier monthly summary
    interface SupplierMonthlySummaryProjection {
        Long getSupplierId();
        String getSupplierName();
        String getContactNumber();
        java.time.LocalDate getLastDelivery();
        java.math.BigDecimal getTotalNetWeight();
        Long getTotalBags();
        Integer getTotalDeliveryDays();
        java.math.BigDecimal getTotalGrossWeight();
    }

    @Query("""
        SELECT 
            s.supplierId AS supplierId,
            u.name AS supplierName,
            u.contactNo AS contactNumber,
            MAX(b.date) AS lastDelivery,
            COALESCE(SUM(b.netWeight), 0) AS totalNetWeight,
            COALESCE(SUM(b.bagTotal), 0) AS totalBags,
            COUNT(DISTINCT b.date) AS totalDeliveryDays,
            COALESCE(SUM(b.grossWeight), 0) AS totalGrossWeight
        FROM BagWeight b
        JOIN b.supplyRequest sr
        JOIN sr.supplier s
        JOIN s.user u
        WHERE s.supplierId = :supplierId
          AND MONTH(b.date) = :month
          AND YEAR(b.date) = :year
        GROUP BY s.supplierId, u.name, u.contactNo
    """)
    SupplierMonthlySummaryProjection getSupplierMonthlySummary(
        @Param("supplierId") Long supplierId,
        @Param("month") int month,
        @Param("year") int year
    );

    // Projection for supplier daily summary
    interface SupplierDailySummaryProjection {
        Integer getDay();
        Long getBagCount();
        java.math.BigDecimal getGrossWeight();
        java.math.BigDecimal getBagWeight();
        java.math.BigDecimal getWater();
        java.math.BigDecimal getCoarseLeaf();
        java.math.BigDecimal getNetWeight();
    }

    @Query("""
        SELECT 
            DAY(b.date) AS day,
            COALESCE(SUM(b.bagTotal), 0) AS bagCount,
            COALESCE(SUM(b.grossWeight), 0) AS grossWeight,
            COALESCE(SUM(b.tareWeight), 0) AS bagWeight,
            COALESCE(SUM(b.water), 0) AS water,
            COALESCE(SUM(b.coarse), 0) AS coarseLeaf,
            COALESCE(SUM(b.netWeight), 0) AS netWeight
        FROM BagWeight b
        JOIN b.supplyRequest sr
        JOIN sr.supplier s
        WHERE s.supplierId = :supplierId
          AND MONTH(b.date) = :month
          AND YEAR(b.date) = :year
        GROUP BY DAY(b.date)
        ORDER BY DAY(b.date)
    """)
    java.util.List<SupplierDailySummaryProjection> getSupplierDailySummary(
        @Param("supplierId") Long supplierId,
        @Param("month") int month,
        @Param("year") int year
    );

    // Projection for supplier weight summary
    interface SupplierWeightSummaryProjection {
        Double getTotalNetWeight();
        Double getTotalGrossWeight();
        Double getTotalWet();
        Double getTotalCoarse();
        Double getTotalTareWeight();
    }

    // Aggregate totals for a supplier, month, and year (PostgreSQL compatible)
    @Query("SELECT " +
            "COALESCE(SUM(b.netWeight),0) AS totalNetWeight, " +
            "COALESCE(SUM(b.grossWeight),0) AS totalGrossWeight, " +
            "COALESCE(SUM(b.water),0) AS totalWet, " +
            "COALESCE(SUM(b.coarse),0) AS totalCoarse, " +
            "COALESCE(SUM(b.tareWeight),0) AS totalTareWeight " +
            "FROM BagWeight b " +
            "WHERE b.supplyRequest.supplier.supplierId = :supplierId " +
            "AND EXTRACT(MONTH FROM b.date) = :month " +
            "AND EXTRACT(YEAR FROM b.date) = :year")
    SupplierWeightSummaryProjection getSupplierWeightSummary(@Param("supplierId") Long supplierId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(b.netWeight), 0) FROM BagWeight b WHERE b.supplyRequest.supplier.supplierId = :supplierId AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year")
    BigDecimal sumWeightBySupplierAndPeriod(@Param("supplierId") Long supplierId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(b.netWeight), 0) FROM BagWeight b WHERE b.supplyRequest.supplier.supplierId = :supplierId AND EXTRACT(MONTH FROM b.date) = :month AND EXTRACT(YEAR FROM b.date) = :year")
    Double sumNetWeightBySupplierIdAndMonthPostgres(@Param("supplierId") String supplierId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT bw FROM BagWeight bw WHERE bw.supplyRequest.requestId = :requestId AND bw.supplyRequest.supplier.supplierId = :supplierId")
    List<BagWeight> findByRequestIdAndSupplierId(@Param("requestId") Long requestId, @Param("supplierId") Long supplierId);
}
