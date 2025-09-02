package com.teafactory.pureleaf.supplier.specs;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import org.springframework.data.jpa.domain.Specification;

public class SupplierRequestSpecs {
    public static Specification<SupplierRequest> hasFactory(Long factoryId) {
        return (root, query, cb) -> cb.equal(root.get("factory").get("factoryId"), factoryId);
    }

    public static Specification<SupplierRequest> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("status")), status.toLowerCase());
    }

    public static Specification<SupplierRequest> searchByNameOrId(String search) {
        if (search == null || search.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> cb.or(
            cb.like(cb.lower(root.get("user").get("name")), "%" + search.toLowerCase() + "%"),
            cb.like(cb.function("str", String.class, root.get("id")), "%" + search + "%")
        );
    }
}

