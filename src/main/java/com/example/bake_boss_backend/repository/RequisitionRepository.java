package com.example.bake_boss_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bake_boss_backend.dto.RequisitionSummaryDTO;
import com.example.bake_boss_backend.entity.Requisition;

public interface RequisitionRepository extends JpaRepository<Requisition, Long>{
    @Query("SELECT r FROM Requisition r WHERE status='pending' AND r.username = :username")
    List<Requisition> getAllRequisitionsByUsername(String username);

    @Query("SELECT new com.example.bake_boss_backend.dto.RequisitionSummaryDTO(r.username, SUM(r.productQty)) " +
    "FROM Requisition r WHERE r.status = 'pending' GROUP BY r.username")
    List<RequisitionSummaryDTO> findSumOfProductQtyGroupedByUsername();

        
}
