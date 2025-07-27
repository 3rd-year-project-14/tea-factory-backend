package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.BagWeighingDTO;
import com.teafactory.pureleaf.service.BagWeighingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bag-weighings")
@CrossOrigin(origins = "*")
public class BagWeighingController {
    @Autowired
    private BagWeighingService bagWeighingService;

    @GetMapping
    public ResponseEntity<List<BagWeighingDTO>> getAllBagWeighings() {
        return ResponseEntity.ok(bagWeighingService.getAllBagWeighings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BagWeighingDTO> getBagWeighing(@PathVariable Long id) {
        BagWeighingDTO bagWeighing = bagWeighingService.getBagWeighing(id);
        if (bagWeighing != null) {
            return ResponseEntity.ok(bagWeighing);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BagWeighingDTO> createBagWeighing(@RequestBody BagWeighingDTO bagWeighingDTO) {
        BagWeighingDTO created = bagWeighingService.createBagWeighing(bagWeighingDTO);
        return ResponseEntity.ok(created);
    }
}

