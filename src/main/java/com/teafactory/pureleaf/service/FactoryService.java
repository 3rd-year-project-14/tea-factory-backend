package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.FactoryDTO;
import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FactoryService {
    @Autowired
    private FactoryRepository factoryRepository;

    public List<FactoryDTO> getAllFactories() {
        return factoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<FactoryDTO> getFactoryById(Long id) {
        return factoryRepository.findById(id)
                .map(this::convertToDTO);
    }

    public FactoryDTO createFactory(FactoryDTO factoryDTO) {
        Factory factory = new Factory();
        factory.setName(factoryDTO.getName());
        factory.setAddress(factoryDTO.getLocation());
        factory.setImage(factoryDTO.getImage());
        factory.setMapUrl(factoryDTO.getMapUrl());
        factory.setCreatedAt(LocalDateTime.now());
        Factory savedFactory = factoryRepository.save(factory);
        return convertToDTO(savedFactory);
    }

    private FactoryDTO convertToDTO(Factory factory) {
        return new FactoryDTO(
            factory.getFactoryId(),
            factory.getName(),
            factory.getAddress(),
            factory.getImage(),
            factory.getMapUrl()
        );
    }
}
