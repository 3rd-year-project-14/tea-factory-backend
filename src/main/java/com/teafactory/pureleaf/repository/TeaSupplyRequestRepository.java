package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.TeaSupplyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeaSupplyRequestRepository extends JpaRepository<TeaSupplyRequest, Long> {
    List<TeaSupplyRequest> findBySupplier_SupplierId(Long supplierId);
}

