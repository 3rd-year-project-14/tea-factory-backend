//package com.teafactory.pureleaf.service;
//
//import com.teafactory.pureleaf.entity.InventoryItem;
//import com.teafactory.pureleaf.repository.InventoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class InventoryService {
//
//    @Autowired
//    private InventoryRepository inventoryRepository;
//
//    public List<InventoryItem> getAll() {
//        return inventoryRepository.findAll();
//    }
//
//    public InventoryItem getById(Long id) {
//        return inventoryRepository.findById(id).orElse(null);
//    }
//
//    public InventoryItem save(InventoryItem item) {
//        return inventoryRepository.save(item);
//    }
//
//    public void delete(Long id) {
//        inventoryRepository.deleteById(id);
//    }
//}
package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.entity.InventoryItem;
import com.teafactory.pureleaf.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public List<InventoryItem> getAll() {
        return inventoryRepository.findAll();
    }

    public InventoryItem getById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public InventoryItem save(InventoryItem item) {
        return inventoryRepository.save(item);
    }

    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }
}
