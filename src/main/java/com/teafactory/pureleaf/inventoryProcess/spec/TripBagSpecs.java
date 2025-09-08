package com.teafactory.pureleaf.inventoryProcess.spec;

import com.teafactory.pureleaf.inventoryProcess.entity.TripBag;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TripBagSpecs {

    public static Specification<TripBag> belongsToTrip(Long tripId) {
        return (root, query, cb) -> cb.equal(root.get("tripSupplier").get("trip").get("tripId"), tripId);
    }

    public static Specification<TripBag> forDate(LocalDate date) {
        return (root, query, cb) -> cb.equal(root.get("tripSupplier").get("trip").get("tripDate"), date);
    }

    public static Specification<TripBag> searchByBagNumber(String search) {
        if (search == null || search.isBlank()) return null;
        String like = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("bag").get("bagNumber")), like);
    }
}

