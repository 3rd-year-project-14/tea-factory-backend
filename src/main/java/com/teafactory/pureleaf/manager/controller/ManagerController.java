package com.teafactory.pureleaf.manager.controller;

import com.teafactory.pureleaf.manager.dto.ManagerInfo;
import com.teafactory.pureleaf.manager.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager-info")
@CrossOrigin(origins = "*")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public ResponseEntity<List<ManagerInfo>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    @GetMapping("/{factoryId}")
    public ResponseEntity<List<ManagerInfo>> getManagersByFactoryId(@PathVariable Long factoryId) {
        return ResponseEntity.ok(managerService.getManagersByFactoryId(factoryId));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
