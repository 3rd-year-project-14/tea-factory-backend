package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.BagWeightDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.BagWeightResponseDTO;
import com.teafactory.pureleaf.inventoryProcess.entity.BagWeight;
import com.teafactory.pureleaf.inventoryProcess.service.BagWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin (origins = "*")
@RestController
@RequestMapping("/api/bagweights")
public class BagWeightController {

    @Autowired
    private BagWeightService bagWeightService;

    @GetMapping
    public List<BagWeight> getAllBagWeights() {
        return bagWeightService.getAllBagWeights();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BagWeight> getBagWeightById(@PathVariable Long id) {
        return bagWeightService.getBagWeightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BagWeightResponseDTO> createBagWeight(@RequestBody BagWeightDTO bagWeightDTO) {
        BagWeight createdBagWeight = bagWeightService.createBagWeight(bagWeightDTO);
        // Update TripBag status to 'weighed' only for the sent bag numbers and supplyRequestId
        bagWeightService.updateTripBagStatusToWeighed(bagWeightDTO.getSupplyRequestId(), bagWeightDTO.getBagNumbers());
        BagWeightResponseDTO responseDTO = mapToResponseDTO(createdBagWeight);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BagWeightResponseDTO> updateBagWeight(@PathVariable Long id, @RequestBody BagWeightDTO bagWeightDTO) {
        BagWeight updatedBagWeight = bagWeightService.updateBagWeight(id, bagWeightDTO);
        BagWeightResponseDTO responseDTO = mapToResponseDTO(updatedBagWeight);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<BagWeightWithSupplierDTO>> getBagWeightsWithSupplierBySessionId(@PathVariable Long sessionId) {
        List<BagWeightWithSupplierDTO> result = bagWeightService.getBagWeightsWithSupplierBySessionId(sessionId);
        return ResponseEntity.ok(result);
    }

    private BagWeightResponseDTO mapToResponseDTO(BagWeight bagWeight) {
        BagWeightResponseDTO dto = new BagWeightResponseDTO();
        dto.setId(bagWeight.getId());
        dto.setCoarse(bagWeight.getCoarse());
        dto.setWater(bagWeight.getWater());
        dto.setDate(bagWeight.getDate());
        dto.setGrossWeight(bagWeight.getGrossWeight());
        dto.setNetWeight(bagWeight.getNetWeight());
        dto.setRecordedAt(bagWeight.getRecordedAt());
        dto.setTareWeight(bagWeight.getTareWeight());
        dto.setOtherWeight(bagWeight.getOtherWeight());
        dto.setReason(bagWeight.getReason());
        dto.setBagTotal(bagWeight.getBagTotal());
        return dto;
    }

}
