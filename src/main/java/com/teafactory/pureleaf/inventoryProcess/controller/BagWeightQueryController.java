package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.BagWeightResponse;
import com.teafactory.pureleaf.inventoryProcess.service.BagWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BagWeightQueryController {

    private final BagWeightService bagWeightService;

    @Autowired
    public BagWeightQueryController(BagWeightService bagWeightService) {
        this.bagWeightService = bagWeightService;
    }

    /**
     * API to get gross weight, net weight, water, and coarse from bag weight table
     * filtered by requestId and supplierId
     * Example: GET /api/bag-weights/by-request-and-supplier?requestId=1&supplierId=2
     */
    @GetMapping("/api/bag-weights/by-request-and-supplier")
    public List<BagWeightResponse> getBagWeightsByRequestAndSupplier(
            @RequestParam Long requestId,
            @RequestParam Long supplierId) {
        return bagWeightService.getBagWeightsBySupplyRequestAndSupplier(requestId, supplierId);
    }
}

