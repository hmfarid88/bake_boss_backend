package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.ReceiveDto;
import com.example.bake_boss_backend.entity.OfficeReceive;

public interface OfficeReceiveRepository extends JpaRepository<OfficeReceive, Long>{
    @Query("SELECT new com.example.bake_boss_backend.dto.ReceiveDto(r.date, r.receiveName, 'OfficeReceive', r.amount) " +
    "FROM OfficeReceive r WHERE r.username=:username AND r.date = :date")
    
List<ReceiveDto> findOfficeReceivesForToday(@Param("username") String username, @Param("date") LocalDate date);
}
