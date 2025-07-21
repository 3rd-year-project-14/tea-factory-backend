package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.WeighingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeighingSessionRepository extends JpaRepository<WeighingSession, Long> {
}

