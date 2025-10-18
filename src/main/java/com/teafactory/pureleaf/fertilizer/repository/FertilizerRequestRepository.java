package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerRequest;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FertilizerRequestRepository extends JpaRepository<FertilizerRequest, Long> {

    List<FertilizerRequest> findByUser_Id(Long userId);

    List<FertilizerRequest> findByStatus(FertilizerRequestStatus status);

    List<FertilizerRequest> findByUser_IdAndStatus(Long userId, FertilizerRequestStatus status);

    @Query("SELECT fr FROM FertilizerRequest fr " +
           "JOIN FETCH fr.category c " +
           "JOIN FETCH fr.company comp " +
           "JOIN FETCH fr.user u " +
           "WHERE fr.id = :id")
    Optional<FertilizerRequest> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT fr FROM FertilizerRequest fr " +
           "JOIN FETCH fr.category c " +
           "JOIN FETCH fr.company comp " +
           "JOIN FETCH fr.user u " +
           "ORDER BY fr.createdAt DESC")
    List<FertilizerRequest> findAllWithDetails();

    @Query("SELECT fr FROM FertilizerRequest fr " +
           "JOIN FETCH fr.category c " +
           "JOIN FETCH fr.company comp " +
           "JOIN FETCH fr.user u " +
           "WHERE fr.status = :status " +
           "ORDER BY fr.createdAt DESC")
    List<FertilizerRequest> findByStatusWithDetails(@Param("status") FertilizerRequestStatus status);
}
