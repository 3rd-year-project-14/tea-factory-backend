package com.teafactory.pureleaf.inventoryProcess.repository;

import com.teafactory.pureleaf.inventoryProcess.entity.TripSupplier;
import com.teafactory.pureleaf.inventoryProcess.dto.TripSupplierId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TripSupplierRepository extends JpaRepository<TripSupplier, TripSupplierId> {
    List<TripSupplier> findByTrip_TripId(Long tripId);
}
