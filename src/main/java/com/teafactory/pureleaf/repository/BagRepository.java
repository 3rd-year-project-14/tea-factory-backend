package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.Bag;
import com.teafactory.pureleaf.entity.Bag.BagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BagRepository extends JpaRepository<Bag, BagId> {
    List<Bag> findByRoute_RouteId(Long routeId);
}

