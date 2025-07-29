package com.teafactory.pureleaf.payment.repository;

import com.teafactory.pureleaf.payment.entity.TeaRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeaRateRepository extends JpaRepository<TeaRate, Long> {
}
