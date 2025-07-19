package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.FactoryDTO;
import com.teafactory.pureleaf.service.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factories")
public class FactoryController {
    @Autowired
    private FactoryService factoryService;

    @GetMapping
    public ResponseEntity<List<FactoryDTO>> getAllFactories() {
        List<FactoryDTO> factoryDTOs = factoryService.getAllFactories();
        return ResponseEntity.ok(factoryDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactoryDTO> getFactoryById(@PathVariable Long id) {
        return factoryService.getFactoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FactoryDTO> createFactory(@RequestBody FactoryDTO factoryDTO) {
        FactoryDTO responseDTO = factoryService.createFactory(factoryDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
