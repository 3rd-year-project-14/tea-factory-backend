package com.teafactory.pureleaf.driverProcess.service;

import com.teafactory.pureleaf.driverProcess.dto.DriverWeightSummaryDTO;
import com.teafactory.pureleaf.inventoryProcess.entity.Trip;
import com.teafactory.pureleaf.inventoryProcess.entity.WeighingSession;
import com.teafactory.pureleaf.inventoryProcess.entity.BagWeight;
import com.teafactory.pureleaf.inventoryProcess.repository.TripRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.WeighingSessionRepository;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverWeightSummaryService {
    private final TripRepository tripRepository;
    private final WeighingSessionRepository weighingSessionRepository;
    private final BagWeightRepository bagWeightRepository;

    @Autowired
    public DriverWeightSummaryService(TripRepository tripRepository,
                                      WeighingSessionRepository weighingSessionRepository,
                                      BagWeightRepository bagWeightRepository) {
        this.tripRepository = tripRepository;
        this.weighingSessionRepository = weighingSessionRepository;
        this.bagWeightRepository = bagWeightRepository;
    }

    public DriverWeightSummaryDTO getWeightSummary(Long driverId, int month, int year) {
        double totalNetWeight = 0;
        double totalGrossWeight = 0;
        double totalWater = 0;
        double totalCoarse = 0;
        double totalTareWeight = 0;
        int totalBagCount = 0;

        List<Trip> trips = tripRepository.findByDriverAndMonthAndYear(driverId, month, year);
        for (Trip trip : trips) {
            List<WeighingSession> sessions = weighingSessionRepository.findByTrip_TripId(trip.getTripId());
            for (WeighingSession session : sessions) {
                List<BagWeight> bagWeights = bagWeightRepository.findByWeighingSession_SessionId(session.getSessionId());
                for (BagWeight bag : bagWeights) {
                    totalNetWeight += bag.getNetWeight();
                    totalGrossWeight += bag.getGrossWeight();
                    totalWater += bag.getWater();
                    totalCoarse += bag.getCoarse();
                    totalTareWeight += bag.getTareWeight();
                    totalBagCount += bag.getBagTotal();
                }
            }
        }
        return new DriverWeightSummaryDTO(totalNetWeight, totalGrossWeight, totalWater, totalCoarse, totalTareWeight, totalBagCount);
    }
}
