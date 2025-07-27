package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.TripBagDTO;
import com.teafactory.pureleaf.service.TripBagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-bags")
@CrossOrigin(origins = "*")
public class TripBagController {
    @Autowired
    private TripBagService tripBagService;

    @GetMapping
    public ResponseEntity<List<TripBagDTO>> getAllTripBags() {
        return ResponseEntity.ok(tripBagService.getAllTripBags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripBagDTO> getTripBag(@PathVariable Long id) {
        TripBagDTO tripBag = tripBagService.getTripBag(id);
        if (tripBag != null) {
            return ResponseEntity.ok(tripBag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TripBagDTO> createTripBag(@RequestBody TripBagDTO tripBagDTO) {
        TripBagDTO created = tripBagService.createTripBag(tripBagDTO);
        return ResponseEntity.ok(created);
    }
}

