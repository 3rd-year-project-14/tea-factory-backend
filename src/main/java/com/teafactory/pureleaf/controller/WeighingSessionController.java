package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.WeighingSessionDTO;
import com.teafactory.pureleaf.service.WeighingSessionService;
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
}
