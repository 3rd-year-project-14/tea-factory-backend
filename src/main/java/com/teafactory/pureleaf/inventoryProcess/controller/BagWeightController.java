package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.BagWeightDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.BagWeightResponseDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.WeighedBagWeightDetailsDTO;
import com.teafactory.pureleaf.inventoryProcess.entity.BagWeight;
import com.teafactory.pureleaf.inventoryProcess.service.BagWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        BagWeightResponseDTO responseDTO = bagWeightService.mapToResponseDTO(createdBagWeight);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BagWeightResponseDTO> updateBagWeight(@PathVariable Long id, @RequestBody BagWeightDTO bagWeightDTO) {
        BagWeight updatedBagWeight = bagWeightService.updateBagWeight(id, bagWeightDTO);
        BagWeightResponseDTO responseDTO = bagWeightService.mapToResponseDTO(updatedBagWeight);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/session/{sessionId}/paged")
    public ResponseEntity<Page<WeighedBagWeightDetailsDTO>> getBagWeightDetailsBySessionIdPaged(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<WeighedBagWeightDetailsDTO> result = bagWeightService.getBagWeightDetailsBySessionIdPaged(sessionId, search, status, pageable);
        return ResponseEntity.ok(result);
    }


}
