package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.PaymentDto;
import com.example.bake_boss_backend.entity.PaymentRecord;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    @Query(value = "SELECT new com.example.bake_boss_backend.dto.PaymentDto(p.date, p.paymentName, p.paymentType, p.amount) "
            + "FROM PaymentRecord p WHERE p.username=:username AND p.date = :date")

    List<PaymentDto> findPaymentsForToday(@Param("username") String username, @Param("date") LocalDate date);
}
