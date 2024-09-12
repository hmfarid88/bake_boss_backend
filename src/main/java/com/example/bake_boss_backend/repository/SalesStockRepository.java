package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.PendingVendorDto;
import com.example.bake_boss_backend.entity.SalesStock;

public interface SalesStockRepository extends JpaRepository<SalesStock, Long> {
        @Query("SELECT ss FROM SalesStock ss WHERE ss.productName = :productName AND ss.username = :username  ORDER BY ss.productId DESC LIMIT 1")
        Optional<SalesStock> findLatestSalesStockByProductNameAndUsername(@Param("productName") String productName,
                        @Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.username = :username AND s.productId = (SELECT MAX(ss.productId) FROM SalesStock ss WHERE ss.productName = s.productName AND ss.username = :username)")
        List<SalesStock> findLastByProductNameAndUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthSoldStocksByUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'vendor' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthVendorSaleByUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthReturnedStocksByUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND  s.date BETWEEN :startDate AND :endDate")
        List<SalesStock> findDatewiseSoldStocksByUsername(String username, LocalDate startDate, LocalDate endDate);

        List<SalesStock> findByProductIdAndUsername(Long productId, String username);

        List<SalesStock> findBySoldInvoice(String soldInvoice);

        @Query("SELECT s.date, s.soldInvoice, SUM(s.productQty * s.saleRate) FROM SalesStock s WHERE s.username = :username AND s.date = :date AND s.status = :status GROUP BY s.soldInvoice")
        List<Object[]> findByUsernameAndDateAndStatus(String username, LocalDate date, String status);

        @Query("SELECT ps FROM SalesStock ps WHERE ps.productName = :productName AND ps.username = :username ORDER BY ps.productId DESC LIMIT 1")
        Optional<SalesStock> findLatestProductStockByProductNameAndUsername(String productName, String username);

        @Query("SELECT s FROM SalesStock s WHERE s.username = :username AND YEAR(s.date) = :year AND MONTH(s.date) = :month")
        List<SalesStock> findCurrentMonthDataByUsername(@Param("username") String username,
                        @Param("year") int year,
                        @Param("month") int month);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthAllReturnedStocks();

        List<SalesStock> findByProductName(String oldItemName);

        @Query("SELECT SUM(s.saleRate*s.productQty) FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND s.date < :date")
        Double findTotalSaleRateByUsernameAndDateBefore(
                        @Param("username") String username,
                        @Param("date") LocalDate date);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND s.date = :date")
        List<SalesStock> findByUsernameAndDate(String username, LocalDate date);

        @Query("SELECT new com.example.bake_boss_backend.dto.PendingVendorDto(s.username, s.soldInvoice, SUM(s.productQty)) " +
       "FROM SalesStock s JOIN CustomerInfo c ON s.soldInvoice = c.soldInvoice " +
       "LEFT JOIN SalesStock ss ON c.soldInvoice = ss.invoiceNo " +
       "WHERE s.status = 'vendor' AND c.customerName = :customerName AND ss.invoiceNo IS NULL " +
       "GROUP BY s.username, s.soldInvoice")
       List<PendingVendorDto> findPendingVendorData(@Param("customerName") String customerName);

        @Query("SELECT ss FROM SalesStock ss WHERE ss.soldInvoice=:soldInvoice AND ss.soldInvoice NOT IN (SELECT s.invoiceNo FROM SalesStock s WHERE s.status='stored')")
        List<SalesStock> findBySoldInvoiceNotInStock(String soldInvoice);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'stored' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthEntryByUsername(@Param("username") String username);
}
