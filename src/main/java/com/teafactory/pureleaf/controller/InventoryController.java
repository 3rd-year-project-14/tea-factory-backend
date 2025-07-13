package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.entity.InventoryItem;
import com.teafactory.pureleaf.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*") // ✅ allow frontend access
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<InventoryItem> getAll() {
        return inventoryService.getAll();
    }

    @GetMapping("/{id}")
    public InventoryItem getById(@PathVariable Long id) {
        return inventoryService.getById(id);
    }

    @PostMapping
    public InventoryItem create(@RequestBody InventoryItem item) {
        return inventoryService.save(item);
    }

    @PutMapping("/{id}")
    public InventoryItem update(@PathVariable Long id, @RequestBody InventoryItem item) {
        item.setId(id);
        return inventoryService.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        inventoryService.delete(id);
    }
}
