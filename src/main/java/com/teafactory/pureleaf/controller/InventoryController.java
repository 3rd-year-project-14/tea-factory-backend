//package com.teafactory.pureleaf.controller;
//
//import com.teafactory.pureleaf.entity.InventoryItem;
//import com.teafactory.pureleaf.service.InventoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/inventory")
//@CrossOrigin(origins = "*") // ✅ allow frontend access
//public class InventoryController {
//
//    @Autowired
//    private InventoryService inventoryService;
//
//    @GetMapping
//    public List<InventoryItem> getAll() {
//        return inventoryService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public InventoryItem getById(@PathVariable Long id) {
//        return inventoryService.getById(id);
//    }
//
//    @PostMapping
//    public InventoryItem create(@RequestBody InventoryItem item) {
//        return inventoryService.save(item);
//    }
//
//    @PutMapping("/{id}")
//    public InventoryItem update(@PathVariable Long id, @RequestBody InventoryItem item) {
//        item.setId(id);
//        return inventoryService.save(item);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        inventoryService.delete(id);
//    }
//}
package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.InventoryItemDTO;
import com.teafactory.pureleaf.entity.InventoryItem;
import com.teafactory.pureleaf.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/inventory")

public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<InventoryItemDTO> getAll() {
        return inventoryService.getAll()
                .stream()
                .map(item -> {
                    InventoryItemDTO dto = new InventoryItemDTO();
                    dto.setId(item.getId());
                    dto.setItem(item.getItem());
                    dto.setQuantity(item.getQuantity());
                    dto.setUnit(item.getUnit());
                    dto.setLastUpdated(item.getLastUpdated());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public InventoryItemDTO getById(@PathVariable Long id) {
        InventoryItem item = inventoryService.getById(id);
        if (item == null) return null;

        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(item.getId());
        dto.setItem(item.getItem());
        dto.setQuantity(item.getQuantity());
        dto.setUnit(item.getUnit());
        dto.setLastUpdated(item.getLastUpdated());
        return dto;
    }

    @PostMapping
    public InventoryItemDTO create(@RequestBody InventoryItemDTO dto) {
        InventoryItem entity = new InventoryItem();
        entity.setItem(dto.getItem());
        entity.setQuantity(dto.getQuantity());
        entity.setUnit(dto.getUnit());

        InventoryItem saved = inventoryService.save(entity);

        InventoryItemDTO responseDto = new InventoryItemDTO();
        responseDto.setId(saved.getId());
        responseDto.setItem(saved.getItem());
        responseDto.setQuantity(saved.getQuantity());
        responseDto.setUnit(saved.getUnit());
        responseDto.setLastUpdated(saved.getLastUpdated());
        return responseDto;
    }

    @PutMapping("/{id}")
    public InventoryItemDTO update(@PathVariable Long id, @RequestBody InventoryItemDTO dto) {
        InventoryItem entity = new InventoryItem();
        entity.setId(id);
        entity.setItem(dto.getItem());
        entity.setQuantity(dto.getQuantity());
        entity.setUnit(dto.getUnit());

        InventoryItem updated = inventoryService.save(entity);

        InventoryItemDTO responseDto = new InventoryItemDTO();
        responseDto.setId(updated.getId());
        responseDto.setItem(updated.getItem());
        responseDto.setQuantity(updated.getQuantity());
        responseDto.setUnit(updated.getUnit());
        responseDto.setLastUpdated(updated.getLastUpdated());
        return responseDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        inventoryService.delete(id);
    }
}
