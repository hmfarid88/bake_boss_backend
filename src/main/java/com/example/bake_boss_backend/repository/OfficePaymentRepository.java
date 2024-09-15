package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.PaymentDto;
import com.example.bake_boss_backend.entity.OfficePayment;

public interface OfficePaymentRepository extends JpaRepository<OfficePayment, Long> {
    @Query(value = "SELECT new com.example.bake_boss_backend.dto.PaymentDto(p.date, p.paymentName, p.paymentNote, p.amount) "
            + "FROM OfficePayment p WHERE p.username=:username AND p.date = :date")
    List<PaymentDto> findPaymentsForToday(@Param("username") String username, @Param("date") LocalDate date);

    @Query("SELECT o FROM OfficePayment o WHERE YEAR(o.date) = :year AND MONTH(o.date) = :month AND o.username = :username")
    List<OfficePayment> findPaymentsByMonth(@Param("year") int year, @Param("month") int month, @Param("username") String username);

    @Query("SELECT o FROM OfficePayment o WHERE o.username = :username AND  o.date BETWEEN :startDate AND :endDate")
    List<OfficePayment> findPaymentsByDate(String username, LocalDate startDate, LocalDate endDate);
}
