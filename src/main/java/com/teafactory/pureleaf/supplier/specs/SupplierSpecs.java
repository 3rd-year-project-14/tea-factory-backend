package com.teafactory.pureleaf.supplier.specs;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecs {

    public static Specification<Supplier> hasFactory(Long factoryId) {
        return (root, query, cb) -> cb.equal(root.get("factory").get("factoryId"), factoryId);
    }

    public static Specification<Supplier> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Supplier> hasRoute(Long routeId) {
        return (root, query, cb) -> cb.equal(root.get("route").get("routeId"), routeId);
    }

    public static Specification<Supplier> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("status")), status.toLowerCase());
    }

    // Name contains (case-insensitive)
    public static Specification<Supplier> nameContains(String search) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("user").get("name")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<Supplier> supplierIdContains(String search) {
        return (root, query, cb) -> {
            try {
                // Convert supplierId (numeric) to string using a database function
                return cb.like(
                        cb.function("str", String.class, root.get("supplierId")),
                        "%" + search + "%"
                );
            } catch (Exception e) {
                return cb.disjunction(); // always false if something goes wrong
            }
        };
    }

    // Combined specification: name OR supplierId
    public static Specification<Supplier> nameOrSupplierId(String search) {
        if (search == null || search.isEmpty()) {
            return null;
        }
        return nameContains(search).or(supplierIdContains(search));
    }



}

