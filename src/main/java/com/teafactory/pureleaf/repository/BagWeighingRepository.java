package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.BagWeighing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagWeighingRepository extends JpaRepository<BagWeighing, Long> {
}

