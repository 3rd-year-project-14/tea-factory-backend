package com.teafactory.pureleaf.inventoryProcess.spec;

import com.teafactory.pureleaf.inventoryProcess.entity.Trip;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Specifications for Trip dynamic filtering (optional alternative to the custom query).
 * Currently not used for the paginated summaries (because those need aggregated bag counts),
 * but available for future extensions (e.g., simple trip list screens without aggregation).
 */
public class TripSpecs {

    public static Specification<Trip> hasFactory(Long factoryId) {
        return (root, query, cb) -> cb.equal(root.get("route").get("factory").get("factoryId"), factoryId);
    }

    public static Specification<Trip> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("status")), status.toLowerCase());
    }

    public static Specification<Trip> tripDateIs(LocalDate date) {
        return (root, query, cb) -> cb.equal(root.get("tripDate"), date);
    }

    /**
     * Search by route name, driver name, or routeId (if numeric).
     */
    public static Specification<Trip> search(String search) {
        if (search == null || search.isBlank()) return null;
        String like = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> {
            var routeJoin = root.join("route");
            var driverJoin = root.join("driver", jakarta.persistence.criteria.JoinType.LEFT);
            var userJoin = driverJoin.join("user", jakarta.persistence.criteria.JoinType.LEFT);

            // route name like
            var p1 = cb.like(cb.lower(routeJoin.get("name")), like);
            // driver name like
            var p2 = cb.like(cb.lower(userJoin.get("name")), like);

            // routeId equals (if numeric)
            Specification<Trip> numericSpec = null;
            try {
                Long routeId = Long.parseLong(search);
                numericSpec = (r2, q2, cb2) -> cb2.equal(r2.get("route").get("routeId"), routeId);
            } catch (NumberFormatException ignored) {}

            if (numericSpec != null) {
                return cb.or(p1, p2, numericSpec.toPredicate(root, query, cb));
            }
            return cb.or(p1, p2);
        };
    }
}

