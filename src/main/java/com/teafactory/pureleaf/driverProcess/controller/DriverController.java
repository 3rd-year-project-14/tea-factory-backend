package com.teafactory.pureleaf.driverProcess.controller;

import com.teafactory.pureleaf.driverProcess.dto.DriverDTO;
import com.teafactory.pureleaf.driverProcess.dto.DriverMonthlyPaymentDTO;
import com.teafactory.pureleaf.driverProcess.dto.DriverRegistrationDTO;
import com.teafactory.pureleaf.driverProcess.dto.DriverWeightSummaryDTO;
import com.teafactory.pureleaf.driverProcess.service.DriverService;
import com.teafactory.pureleaf.driverProcess.service.DriverWeightSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverWeightSummaryService driverWeightSummaryService;


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

    @PostMapping("/register")
    public ResponseEntity<DriverDTO> registerDriver(@RequestBody DriverRegistrationDTO registrationDTO) {
        DriverDTO created = driverService.registerDriver(registrationDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverDTO> getDriverDetailsByUserId(@PathVariable Long userId) {
        DriverDTO dto = driverService.getDriverDetailsByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/monthly-payment")
    public ResponseEntity<DriverMonthlyPaymentDTO> getMonthlyPayment(
            @PathVariable("id") Long driverId,
            @RequestParam("month") int month,
            @RequestParam("year") int year) {
        DriverWeightSummaryDTO summary = driverWeightSummaryService.getWeightSummary(driverId, month, year);
        double netWeight = summary.getTotalNetWeight();
        double payment = netWeight * 3.0;
        DriverMonthlyPaymentDTO dto = new DriverMonthlyPaymentDTO(driverId, month, year, netWeight, payment);
        return ResponseEntity.ok(dto);
    }
}
