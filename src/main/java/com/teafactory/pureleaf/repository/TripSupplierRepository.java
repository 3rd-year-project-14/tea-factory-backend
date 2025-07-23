package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.TripSupplier;
import com.teafactory.pureleaf.entity.TripSupplier.TripSupplierId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TripSupplierRepository extends JpaRepository<TripSupplier, TripSupplierId> {
    List<TripSupplier> findByTrip_TripId(Long tripId);
}

