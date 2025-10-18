package com.teafactory.pureleaf.advanceProcess.spec;

import com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class AdvanceSpecification {
    public static Specification<SupplierAdvance> filterAdvances(Long factoryId, String status, Integer month, Integer year, String search) {
        return (Root<SupplierAdvance> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (factoryId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("supplier").get("factory").get("factoryId"), factoryId));
            }
            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), SupplierAdvance.Status.valueOf(status.toUpperCase())));
            }
            if (month != null) {
                predicate = cb.and(predicate, cb.equal(
                    cb.function("date_part", Integer.class, cb.literal("month"), root.get("requestedDate")), month));
            }
            if (year != null) {
                predicate = cb.and(predicate, cb.equal(
                    cb.function("date_part", Integer.class, cb.literal("year"), root.get("requestedDate")), year));
            }
            if (search != null && !search.isEmpty()) {
                // Search by supplier name
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("supplier").get("user").get("name")), "%" + search.toLowerCase() + "%"));
            }
            return predicate;
        };
    }
}
