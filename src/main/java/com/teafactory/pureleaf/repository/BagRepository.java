package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.Bag;
import com.teafactory.pureleaf.entity.Bag.BagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BagRepository extends JpaRepository<Bag, BagId> {
    List<Bag> findByRoute_RouteId(Long routeId);
    List<Bag> findByRoute_RouteIdAndStatus(Long routeId, String status);

    // Get the highest (latest) bag number for a given route
    @Query("select max(b.bagNumber) from Bag b where b.routeId = :routeId")
    String findMaxBagNumberByRouteId(@Param("routeId") Long routeId);
}
