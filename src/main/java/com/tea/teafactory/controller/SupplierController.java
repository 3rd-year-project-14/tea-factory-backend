package com.tea.teafactory.controller;

import com.tea.teafactory.model.Supplier;
import com.tea.teafactory.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    @PostMapping
    public Supplier create(@RequestBody Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @GetMapping("/{id}")
    public Supplier getOne(@PathVariable Long id) {
        return supplierRepository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Supplier update(@PathVariable Long id, @RequestBody Supplier supplier) {
        Supplier s = supplierRepository.findById(id).orElseThrow();
        s.setNo(supplier.getNo());
        s.setName(supplier.getName());
        s.setWeight(supplier.getWeight());
        return supplierRepository.save(s);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        supplierRepository.deleteById(id);
    }
}
