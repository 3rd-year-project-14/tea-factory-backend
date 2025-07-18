package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
}
