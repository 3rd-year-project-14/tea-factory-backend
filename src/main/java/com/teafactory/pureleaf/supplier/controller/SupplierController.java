package com.teafactory.pureleaf.supplier.controller;


import com.teafactory.pureleaf.supplier.dto.SupplierCountDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierDTO;
import com.teafactory.pureleaf.supplier.dto.ActiveSuppliersDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierDetailsDTO;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import com.teafactory.pureleaf.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierService supplierService;

    // Retrieves active suppliers for a specific factory with pagination, search, and filters
    @GetMapping("/active/factory/{factoryId}")
    public ResponseEntity<?> getActiveSuppliers(
            @PathVariable Long factoryId,
            @RequestParam(required = false) Long routeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "approvedDate,desc") String[] sort
    ) {
        String sortBy = sort[0];
        String sortDir = sort.length > 1 ? sort[1] : "asc";

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)
        );

        Page<ActiveSuppliersDTO> suppliers = supplierService.getActiveSuppliers(factoryId, routeId, status, search, pageable);

        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }


    // Gets supplier count statistics for a factory
    @GetMapping("/count/{factoryId}")
    public ResponseEntity<SupplierCountDTO> getSuppliersCountsByFactoryId(@PathVariable Long factoryId) {
            SupplierCountDTO counts = supplierService.getSuppliersCounts(factoryId);
            return new ResponseEntity<>(counts, HttpStatus.OK);
    }

    // Retrieves detailed information for a supplier by ID
    @GetMapping("/details/{supplierId}")
    public ResponseEntity<SupplierDetailsDTO> getSupplierDetailsById(@PathVariable Long supplierId) {
        SupplierDetailsDTO supplierDetails = supplierService.getSupplierDetails(supplierId);
        return new ResponseEntity<>(supplierDetails, HttpStatus.OK);
    }
}
