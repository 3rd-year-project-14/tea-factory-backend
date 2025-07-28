package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.TeaSupplyProcessResponseDTO;
import com.teafactory.pureleaf.entity.Driver;
import com.teafactory.pureleaf.entity.Route;
import com.teafactory.pureleaf.entity.TeaSupplyRequest;
import com.teafactory.pureleaf.repository.DriverRepository;
import com.teafactory.pureleaf.repository.RouteRepository;
import com.teafactory.pureleaf.repository.TeaSupplyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeaSupplyProcessService {
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TeaSupplyRequestRepository teaSupplyRequestRepository;

    public TeaSupplyProcessResponseDTO simulateAfterFour(Long driverId) {
        // Find route assigned to the driver
        Route route = routeRepository.findFirstByDriver_DriverId(driverId);
        if (route == null || route.getFactory() == null) {
            return new TeaSupplyProcessResponseDTO(); // or handle error
        }
        Long factoryId = route.getFactory().getFactoryId();
        Long routeId = route.getRouteId();
        LocalDate today = LocalDate.now();
        List<TeaSupplyRequest> requests = teaSupplyRequestRepository
            .findTodayTeaSuppliers(
                today,
                factoryId,
                routeId
            );
        List<TeaSupplyProcessResponseDTO.TeaSupplyRequestInfo> requestInfos = requests.stream()
            .map(r -> new TeaSupplyProcessResponseDTO.TeaSupplyRequestInfo(
                r.getRequestId(),
                r.getEstimatedBagCount(),
                r.getSupplier().getSupplierId(),
                r.getSupplier().getUser() != null ? r.getSupplier().getUser().getName() : null
            ))
            .collect(Collectors.toList());
        return new TeaSupplyProcessResponseDTO(requestInfos);
    }
}
