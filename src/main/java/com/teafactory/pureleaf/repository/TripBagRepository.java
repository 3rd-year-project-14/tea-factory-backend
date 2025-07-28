package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.TripBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripBagRepository extends JpaRepository<TripBag, Long> {
}

