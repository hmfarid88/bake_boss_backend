package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.entity.SalesStock;

public interface SalesStockRepository extends JpaRepository<SalesStock, Long> {
    @Query("SELECT ss FROM SalesStock ss WHERE ss.productName = :productName AND ss.username = :username  ORDER BY ss.productId DESC LIMIT 1")
    Optional<SalesStock> findLatestSalesStockByProductNameAndUsername(@Param("productName") String productName,
            @Param("username") String username);

    @Query("SELECT s FROM SalesStock s WHERE s.username = :username AND s.productId = (SELECT MAX(ss.productId) FROM SalesStock ss WHERE ss.productName = s.productName AND ss.username = :username)")
    List<SalesStock> findLastByProductNameAndUsername(@Param("username") String username);

    @Query("SELECT s FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
    List<SalesStock> findCurrentMonthSoldStocksByUsername(@Param("username") String username);

    // @Query("SELECT s FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND  s.date between startDate AND endDate")
    // List<SalesStock> findDatewiseSoldStocksByUsername(@Param("username") String username, @Param("starDate") String startDate, @Param("endDate") String endDate);

    List<SalesStock> findByProductIdAndUsername(Long productId, String username);

    List<SalesStock> findBySoldInvoice(String soldInvoice);

    @Query("SELECT s.date, s.soldInvoice, SUM(s.productQty * s.saleRate) FROM SalesStock s WHERE s.username = :username AND s.date = :date AND s.status = :status GROUP BY s.soldInvoice")
    List<Object[]> findByUsernameAndDateAndStatus(String username, LocalDate date, String status);

}
