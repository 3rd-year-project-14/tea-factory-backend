package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByUser_Id(Long userId);
}
