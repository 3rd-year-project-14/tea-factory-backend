package com.teafactory.pureleaf.routes.controller;

import com.teafactory.pureleaf.routes.dto.CreateRouteDTO;
import com.teafactory.pureleaf.routes.dto.RouteDetailsDTO;
import com.teafactory.pureleaf.routes.entity.Route;
import com.teafactory.pureleaf.routes.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/routes")
public class RouteController {
    @Autowired
    private RouteService routeService;

    @GetMapping("/factory/{factoryId}")
    public List<RouteDetailsDTO> getRoutesByFactory(@PathVariable Long factoryId) {
        return routeService.findRoutesDetailsByFactoryId(factoryId);
    }

    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody CreateRouteDTO createRouteDTO) {
        Route created = routeService.createRoute(createRouteDTO);
        return ResponseEntity.ok(created);
    }
}
