package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.PaymentDto;
import com.example.bake_boss_backend.entity.SupplierPayment;

public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
    @Query("SELECT new com.example.bake_boss_backend.dto.PaymentDto(s.date, s.supplierName, 'SupplierPayment', s.amount) "
            + "FROM SupplierPayment s WHERE s.username = :username AND s.date = :date")

    List<PaymentDto> findSupplierPaymentsForToday(@Param("username") String username, @Param("date") LocalDate date);
}
