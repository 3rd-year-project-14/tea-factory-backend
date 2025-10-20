package com.teafactory.pureleaf.supplierMobileApp.service;

import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import com.teafactory.pureleaf.supplierMobileApp.dto.WeightSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierWeightSummaryService {
    private final BagWeightRepository bagWeightRepository;

    @Autowired
    public SupplierWeightSummaryService(BagWeightRepository bagWeightRepository) {
        this.bagWeightRepository = bagWeightRepository;
    }

    public WeightSummaryDTO getWeightSummary(Long supplierId, int month, int year) {
        var projection = bagWeightRepository.getSupplierWeightSummary(supplierId, month, year);
        if (projection == null) {
            return new WeightSummaryDTO(0, 0, 0, 0, 0);
        }
        return new WeightSummaryDTO(
                projection.getTotalNetWeight() != null ? projection.getTotalNetWeight() : 0,
                projection.getTotalGrossWeight() != null ? projection.getTotalGrossWeight() : 0,
                projection.getTotalWet() != null ? projection.getTotalWet() : 0,
                projection.getTotalCoarse() != null ? projection.getTotalCoarse() : 0,
                projection.getTotalTareWeight() != null ? projection.getTotalTareWeight() : 0
        );
    }
}

