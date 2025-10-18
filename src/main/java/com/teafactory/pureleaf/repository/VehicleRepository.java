// ...existing code...
package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}

