package com.teafactory.pureleaf.advance.repository;

import com.teafactory.pureleaf.advance.entity.AdvanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvanceRequestRepository extends JpaRepository<AdvanceRequest, Long> {
    List<AdvanceRequest> findByStatus(AdvanceRequest.Status status);

}