package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.BagDTO;
import com.teafactory.pureleaf.dto.GenerateBagsRequest;
import com.teafactory.pureleaf.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bags")
@Validated
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
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBag);
    }

    @GetMapping("/route/{routeId}/not-assigned-bag-numbers")
    public ResponseEntity<List<String>> getNotAssignedBagNumbersByRouteId(@PathVariable Long routeId) {
        List<String> bagNumbers = bagService.getNotAssignedBagNumbersByRouteId(routeId);
        return ResponseEntity.ok(bagNumbers);
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateBags(@Valid @RequestBody GenerateBagsRequest request) {
        List<BagDTO> created = bagService.generateBagsForRoute(request.getRouteId(), request.getQuantity());
        Map<String, Object> body = new HashMap<>();
        body.put("routeId", request.getRouteId());
        body.put("createdCount", created.size());
        if (!created.isEmpty()) {
            body.put("startBagNumber", created.get(0).getBagNumber());
            body.put("endBagNumber", created.get(created.size() - 1).getBagNumber());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
