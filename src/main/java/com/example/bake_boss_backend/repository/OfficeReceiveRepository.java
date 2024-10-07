package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.ReceiveDto;
import com.example.bake_boss_backend.entity.OfficeReceive;

public interface OfficeReceiveRepository extends JpaRepository<OfficeReceive, Long> {
    @Query("SELECT new com.example.bake_boss_backend.dto.ReceiveDto(r.date, r.receiveName, r.receiveNote, r.amount) " +
            "FROM OfficeReceive r WHERE r.username=:username AND r.date = :date order by r.date asc")
    List<ReceiveDto> findOfficeReceivesForToday(@Param("username") String username, @Param("date") LocalDate date);

    @Query("SELECT o FROM OfficeReceive o WHERE YEAR(o.date) = :year AND MONTH(o.date) = :month AND o.username = :username order by o.date asc")
    List<OfficeReceive> findReceiveByMonth(@Param("year") int year, @Param("month") int month, @Param("username") String username);

    @Query("SELECT o FROM OfficeReceive o WHERE o.username = :username AND  o.date BETWEEN :startDate AND :endDate order by o.date asc")
    List<OfficeReceive> findReceiveByDate(String username, LocalDate startDate, LocalDate endDate);
}
