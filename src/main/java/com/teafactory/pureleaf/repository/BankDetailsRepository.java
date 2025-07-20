package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {
}

