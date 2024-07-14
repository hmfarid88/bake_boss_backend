package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.ReceiveDto;
import com.example.bake_boss_backend.entity.RetailerPayment;

public interface RetailerPaymentRepository extends JpaRepository<RetailerPayment, Long>{
    @Query(value = "SELECT ( " +
            "  (SELECT COALESCE(SUM(amount), 0) FROM retailer_payment WHERE username = :username AND DATE(date) < :date) + " +
            "  (SELECT COALESCE(SUM(amount), 0) FROM office_receive WHERE username = :username AND DATE(date) < :date) " +
            ") - ( " +
            "  (SELECT COALESCE(SUM(amount), 0) FROM payment_record WHERE username = :username AND DATE(date) < :date) + " +
            "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_payment WHERE username = :username AND DATE(date) < :date) " +
            ") AS total_amount", nativeQuery = true)
    Double findNetSumAmountBeforeToday(@Param("username") String username, @Param("date") LocalDate date);

      @Query("SELECT new com.example.bake_boss_backend.dto.ReceiveDto(rp.date, rp.retailerName, rp.note, rp.amount) " +
            "FROM RetailerPayment rp " +
            "WHERE rp.username = :username AND rp.date = :date")
    List<ReceiveDto> findRetailerPaymentsForToday(@Param("username") String username, @Param("date") LocalDate date);
}
