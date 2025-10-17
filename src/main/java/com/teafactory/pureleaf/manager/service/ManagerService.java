package com.teafactory.pureleaf.manager.service;

import com.teafactory.pureleaf.manager.dto.ManagerInfo;
import com.teafactory.pureleaf.manager.repository.ManagerJdbcRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    private final ManagerJdbcRepository repository;

    public ManagerService(ManagerJdbcRepository repository) {
        this.repository = repository;
    }

    public List<ManagerInfo> getAllManagers() {
        return repository.findAllManagers();
    }

    public List<ManagerInfo> getManagersByFactoryId(Long factoryId) {
        return repository.findManagersByFactoryId(factoryId);
    }
}

