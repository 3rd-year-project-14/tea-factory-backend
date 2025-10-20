package com.teafactory.pureleaf.driverProcess.controller;

import com.teafactory.pureleaf.driverProcess.dto.DriverWeightSummaryDTO;
import com.teafactory.pureleaf.driverProcess.service.DriverWeightSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/driverMobileApp/weights")
public class DriverWeightSummaryController {
    private final DriverWeightSummaryService summaryService;

    @Autowired
    public DriverWeightSummaryController(DriverWeightSummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/summary")
    public DriverWeightSummaryDTO getWeightSummary(
            @RequestParam Long driverId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return summaryService.getWeightSummary(driverId, month, year);
    }
}

