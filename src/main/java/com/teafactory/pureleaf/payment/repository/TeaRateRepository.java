package com.teafactory.pureleaf.payment.repository;
import com.teafactory.pureleaf.payment.entity.TeaRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TeaRateRepository extends JpaRepository<TeaRate, Long> {

    @Query(value = "SELECT final_rate_per_kg FROM tea_rate WHERE month = :month AND status = 'APPROVED' ORDER BY calculated_date DESC NULLS LAST LIMIT 1", nativeQuery = true)
    BigDecimal findCurrentRateByDate(@Param("month") String month);

}
