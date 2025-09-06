package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.TeaSupplyProcessResponseDTO;
import com.teafactory.pureleaf.inventoryProcess.service.TeaSupplyProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tea-supply-today")
public class TeaSupplyProcessController {

    @Autowired
    private TeaSupplyProcessService teaSupplyProcessService;

    @GetMapping("/{driverId}")
    public TeaSupplyProcessResponseDTO simulateAfterFour(@PathVariable Long driverId) {
        return teaSupplyProcessService.simulateAfterFour(driverId);
    }
}

