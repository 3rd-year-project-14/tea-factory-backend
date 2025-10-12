package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.WeighingSessionDTO;
import com.teafactory.pureleaf.inventoryProcess.service.WeighingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/weighing-sessions")
public class WeighingSessionController {
    @Autowired
    private WeighingSessionService weighingSessionService;

    @GetMapping
    public List<WeighingSessionDTO> getAllWeighingSessions() {
        return weighingSessionService.getAllWeighingSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeighingSessionDTO> getWeighingSessionById(@PathVariable Long id) {
        WeighingSessionDTO dto = weighingSessionService.getWeighingSessionById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<WeighingSessionDTO> getWeighingSessionByTripId(@PathVariable Long tripId) {
        WeighingSessionDTO dto = weighingSessionService.getWeighingSessionsByTripId(tripId)
            .stream().findFirst().orElse(null);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<WeighingSessionDTO> createWeighingSession(@RequestBody WeighingSessionDTO weighingSessionDTO) {
        WeighingSessionDTO created = weighingSessionService.createWeighingSession(weighingSessionDTO);
        return ResponseEntity.ok(created);
    }
}
