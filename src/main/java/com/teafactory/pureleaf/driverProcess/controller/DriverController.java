package com.teafactory.pureleaf.driverProcess.controller;

import com.teafactory.pureleaf.driverProcess.dto.DriverDTO;
import com.teafactory.pureleaf.driverProcess.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;


    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO driverDTO) {
        DriverDTO createdDriver = driverService.createDriver(driverDTO);
        return ResponseEntity.ok(createdDriver);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverDTO> getDriverDetailsByUserId(@PathVariable Long userId) {
        DriverDTO dto = driverService.getDriverDetailsByUserId(userId);
        return ResponseEntity.ok(dto);
    }
}
