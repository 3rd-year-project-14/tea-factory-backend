package com.teafactory.pureleaf.payment.repository;

import com.teafactory.pureleaf.payment.entity.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
}

