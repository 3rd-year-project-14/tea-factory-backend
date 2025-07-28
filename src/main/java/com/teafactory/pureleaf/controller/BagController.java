package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.BagDTO;
import com.teafactory.pureleaf.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bags")
public class BagController {
    @Autowired
    private BagService bagService;

    @GetMapping
    public ResponseEntity<List<BagDTO>> getAllBags() {
        List<BagDTO> bags = bagService.getAllBags();
        return ResponseEntity.ok(bags);
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<BagDTO>> getBagsByRouteId(@PathVariable Long routeId) {
        List<BagDTO> bags = bagService.getBagsByRouteId(routeId);
        return ResponseEntity.ok(bags);
    }

    @GetMapping("/{routeId}/{bagNumber}")
    public ResponseEntity<BagDTO> getBagByRouteIdAndBagNumber(@PathVariable Long routeId, @PathVariable String bagNumber) {
        BagDTO bag = bagService.getBagByRouteIdAndBagNumber(routeId, bagNumber);
        if (bag != null) {
            return ResponseEntity.ok(bag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BagDTO> createBag(@RequestBody BagDTO bagDTO) {
        BagDTO createdBag = bagService.createBag(bagDTO);
        return ResponseEntity.ok(createdBag);
    }
}
