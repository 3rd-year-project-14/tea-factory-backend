package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.WeighingSessionDTO;
import com.teafactory.pureleaf.entity.WeighingSession;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.entity.Trip;
import com.teafactory.pureleaf.repository.WeighingSessionRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import com.teafactory.pureleaf.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeighingSessionService {
    @Autowired
    private WeighingSessionRepository weighingSessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;

    public List<WeighingSessionDTO> getAllWeighingSessions() {
        return weighingSessionRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public WeighingSessionDTO getWeighingSessionById(Long id) {
        Optional<WeighingSession> sessionOpt = weighingSessionRepository.findById(id);
        return sessionOpt.map(this::toDTO).orElse(null);
    }

    public List<WeighingSessionDTO> getWeighingSessionsByTripId(Long tripId) {
        return weighingSessionRepository.findByTrip_TripId(tripId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public WeighingSessionDTO createWeighingSession(WeighingSessionDTO weighingSessionDTO) {
        WeighingSession session = new WeighingSession();
        session.setSessionDate(LocalDate.now());
        session.setStartTime(LocalTime.now());
        session.setStatus("pending");
        if (weighingSessionDTO.getUserId() != null) {
            User user = userRepository.findById(weighingSessionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            session.setUser(user);
        }
        if (weighingSessionDTO.getTripId() != null) {
            Trip trip = tripRepository.findById(weighingSessionDTO.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));
            session.setTrip(trip);
        }
        WeighingSession saved = weighingSessionRepository.save(session);
        return toDTO(saved);
    }

    private WeighingSessionDTO toDTO(WeighingSession session) {
        WeighingSessionDTO dto = new WeighingSessionDTO();
        dto.setSessionId(session.getSessionId());
        if (session.getTrip() != null) {
            dto.setTripId(session.getTrip().getTripId());
        }
        dto.setSessionDate(session.getSessionDate());
        if (session.getUser() != null) {
            dto.setUserId(session.getUser().getId());
            dto.setUserName(session.getUser().getName());
        }
        dto.setStatus(session.getStatus());
        return dto;
    }
}
