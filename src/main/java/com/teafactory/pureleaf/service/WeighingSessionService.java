package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.WeighingSessionDTO;
import com.teafactory.pureleaf.entity.WeighingSession;
import com.teafactory.pureleaf.repository.WeighingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeighingSessionService {
    @Autowired
    private WeighingSessionRepository weighingSessionRepository;

    public List<WeighingSessionDTO> getAllWeighingSessions() {
        return weighingSessionRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public WeighingSessionDTO getWeighingSessionById(Long id) {
        Optional<WeighingSession> sessionOpt = weighingSessionRepository.findById(id);
        return sessionOpt.map(this::toDTO).orElse(null);
    }

    private WeighingSessionDTO toDTO(WeighingSession session) {
        WeighingSessionDTO dto = new WeighingSessionDTO();
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
