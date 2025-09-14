package com.teafactory.pureleaf.inventoryProcess.spec;

import com.teafactory.pureleaf.inventoryProcess.entity.BagWeight;
import org.springframework.data.jpa.domain.Specification;

public class BagWeightSpecs {
    public static Specification<BagWeight> hasSessionId(Long sessionId) {
        return (root, query, cb) -> cb.equal(root.get("weighingSession").get("sessionId"), sessionId);
    }

    private static Specification<BagWeight> supplierNameContains(String search) {
        return (root, query, cb) ->
            cb.like(cb.lower(root.get("supplyRequest").get("supplier").get("user").get("name")), "%" + search.toLowerCase() + "%");
    }

    private static Specification<BagWeight> supplierIdContains(String search) {
        return (root, query, cb) -> {
            // Convert supplierId (numeric) to string using concatenation
            return cb.like(
                cb.concat(root.get("supplyRequest").get("supplier").get("supplierId").as(String.class), cb.literal("")),
                "%" + search + "%"
            );
        };
    }

    public static Specification<BagWeight> searchSupplier(String search) {
        if (search == null || search.trim().isEmpty()) {
            return null;
        }
        return supplierNameContains(search).or(supplierIdContains(search));
    }

    public static Specification<BagWeight> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("supplyRequest").get("status")), status.trim().toLowerCase());
        };
    }
}
