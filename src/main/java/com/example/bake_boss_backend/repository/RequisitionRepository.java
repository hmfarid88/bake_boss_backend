package com.example.bake_boss_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.bake_boss_backend.dto.RequisitionSummaryDTO;
import com.example.bake_boss_backend.entity.Requisition;

import jakarta.transaction.Transactional;

public interface RequisitionRepository extends JpaRepository<Requisition, Long> {
    @Query("SELECT r FROM Requisition r WHERE status='pending' AND r.username = :username")
    List<Requisition> getAllRequisitionsByUsername(String username);

    @Query("SELECT new com.example.bake_boss_backend.dto.RequisitionSummaryDTO(r.username, SUM(r.productQty)) " +
            "FROM Requisition r WHERE r.status = 'pending' GROUP BY r.username")
    List<RequisitionSummaryDTO> findSumOfProductQtyGroupedByUsername();

    @Transactional
    @Modifying
    @Query("UPDATE Requisition r SET r.status = :status WHERE r.reqId = :reqId")
    int updateStatusByReqId(Long reqId, String status);

}
