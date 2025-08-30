//package com.teafactory.pureleaf.service;
//
//import com.teafactory.pureleaf.dto.ManagerDTO;
//import com.teafactory.pureleaf.entity.Manager;
//import com.teafactory.pureleaf.repository.ManagerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ManagerService {
//
//    @Autowired
//    private ManagerRepository managerRepository;
//
//    public void saveManager(ManagerDTO managerDTO) {
//        Manager manager = new Manager();
//        manager.setName(managerDTO.getName());
//        manager.setPassword(managerDTO.getPassword()); // Hash before saving in production
//        manager.setEmail(managerDTO.getEmail());
//        manager.setNic(managerDTO.getNic());
//        manager.setMobile(managerDTO.getMobile());
//        manager.setRole(managerDTO.getRole());
//        manager.setFactory(managerDTO.getFactory());
//
//        managerRepository.save(manager);
//    }
//}