package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.TopSalesDTO;
import com.example.bake_boss_backend.dto.LossProfitAnalysis;
import com.example.bake_boss_backend.dto.PendingVendorDto;
import com.example.bake_boss_backend.dto.SalesProfitDto;
import com.example.bake_boss_backend.dto.SixMonthSaleDTO;
import com.example.bake_boss_backend.entity.SalesStock;

public interface SalesStockRepository extends JpaRepository<SalesStock, Long> {
        // @Query("SELECT ss FROM SalesStock ss WHERE ss.productName = :productName AND
        // ss.username = :username ORDER BY ss.productId DESC LIMIT 1")
        // Optional<SalesStock>
        // findLatestSalesStockByProductNameAndUsername(@Param("productName") String
        // productName, @Param("username") String username);

        Optional<SalesStock> findTopByProductNameAndUsernameOrderByProductIdDesc(String productName, String username);

        @Query("SELECT s FROM SalesStock s WHERE s.username = :username AND s.productId IN (SELECT MAX(ss.productId) FROM SalesStock ss WHERE ss.username = :username GROUP BY ss.productName) ORDER BY s.productName ASC")
        List<SalesStock> findLastByProductNameAndUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthSoldStocksByUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'vendor' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthVendorSaleByUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'vendor' AND s.username = :username AND  s.date BETWEEN :startDate AND :endDate")
        List<SalesStock> findDatewiseVendorSaleByUsername(String username, LocalDate startDate, LocalDate endDate);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthReturnedStocksByUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND s.username = :username AND s.date BETWEEN :startDate AND :endDate")
        List<SalesStock> findDatewiseReturnedStocksByUsername(String username, LocalDate startDate, LocalDate endDate);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND  s.date BETWEEN :startDate AND :endDate")
        List<SalesStock> findDatewiseSoldStocksByUsername(String username, LocalDate startDate, LocalDate endDate);

        List<SalesStock> findByProductIdAndUsername(Long productId, String username);

        List<SalesStock> findBySoldInvoice(String soldInvoice);

        @Query("SELECT s.date, s.soldInvoice, SUM((s.productQty * s.saleRate) - COALESCE(s.discount, 0)) " +
                        "FROM SalesStock s WHERE s.username = :username AND s.date = :date AND s.status = :status GROUP BY s.date, s.soldInvoice")
        List<Object[]> findByUsernameAndDateAndStatus(String username, LocalDate date, String status);

        @Query("SELECT ps FROM SalesStock ps WHERE ps.productName = :productName AND ps.username = :username ORDER BY ps.productId DESC LIMIT 1")
        Optional<SalesStock> findLatestProductStockByProductNameAndUsername(String productName, String username);

        @Query("SELECT s FROM SalesStock s WHERE s.username = :username AND YEAR(s.date) = :year AND MONTH(s.date) = :month")
        List<SalesStock> findCurrentMonthDataByUsername(@Param("username") String username, @Param("year") int year,
                        @Param("month") int month);

        @Query("SELECT s FROM SalesStock s WHERE s.username = :username AND  s.date BETWEEN :startDate AND :endDate")
        List<SalesStock> findDatewiseStockLedgerUsername(String username, LocalDate startDate, LocalDate endDate);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthAllReturnedStocks();

        List<SalesStock> findByProductName(String oldItemName);

        @Query("SELECT SUM(s.productQty * s.saleRate) - SUM(COALESCE(s.discount, 0)) " +
                        "FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND s.date < :date")
        Double findTotalSaleRateByUsernameAndDateBefore(@Param("username") String username,
                        @Param("date") LocalDate date);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND s.date = :date")
        List<SalesStock> findByUsernameAndDate(String username, LocalDate date);

        @Query("SELECT new com.example.bake_boss_backend.dto.PendingVendorDto(s.username, s.soldInvoice, SUM(s.productQty)) "
                        +
                        "FROM SalesStock s JOIN CustomerInfo c ON s.soldInvoice = c.soldInvoice " +
                        "LEFT JOIN SalesStock ss ON c.soldInvoice = ss.invoiceNo " +
                        "WHERE s.status = 'vendor' AND c.customerName = :customerName AND ss.invoiceNo IS NULL " +
                        "GROUP BY s.username, s.soldInvoice")
        List<PendingVendorDto> findPendingVendorData(@Param("customerName") String customerName);

        @Query("SELECT ss FROM SalesStock ss WHERE ss.soldInvoice=:soldInvoice AND ss.soldInvoice NOT IN (SELECT s.invoiceNo FROM SalesStock s WHERE s.status='stored')")
        List<SalesStock> findBySoldInvoiceNotInStock(String soldInvoice);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'stored' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
        List<SalesStock> findCurrentMonthEntryByUsername(@Param("username") String username);

        @Query("SELECT s FROM SalesStock s WHERE s.status = 'stored' AND s.username = :username AND  s.date BETWEEN :startDate AND :endDate")
        List<SalesStock> findDatewiseEntryByUsername(String username, LocalDate startDate, LocalDate endDate);

        @Modifying
        @Query("UPDATE SalesStock s SET s.remainingQty = s.remainingQty - :qtyDifference " +
                        "WHERE s.productName = :productName AND s.productId >= :productId")
        void reduceRemainingQty(@Param("productName") String productName,
                        @Param("productId") Long productId,
                        @Param("qtyDifference") Double qtyDifference);

        @Modifying
        @Query("UPDATE SalesStock s SET s.remainingQty = s.remainingQty + :qtyDifference " +
                        "WHERE s.productName = :productName AND s.productId >= :productId")
        void increaseRemainingQty(@Param("productName") String productName, @Param("productId") Long productId,
                        @Param("qtyDifference") Double qtyDifference);

        List<SalesStock> findByProductNameAndProductIdGreaterThan(String productName, Long productId);

        @Query("SELECT new com.example.bake_boss_backend.dto.SalesProfitDto(s.date, s.category, s.productName, SUM(s.costPrice), SUM(s.saleRate), SUM(s.productQty), SUM(s.discount)) "
                        +
                        "FROM SalesStock s WHERE s.status='sold' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE) "
                        +
                        "GROUP BY s.date, s.category, s.productName")
        List<SalesProfitDto> findMonthlyProfit(@Param("username") String username);

        @Query("SELECT new com.example.bake_boss_backend.dto.SalesProfitDto(s.date, s.category, s.productName, SUM(s.costPrice), SUM(s.saleRate), SUM(s.productQty), SUM(s.discount)) "
                        +
                        "FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND s.date BETWEEN :startDate AND :endDate "
                        +
                        "GROUP BY s.date, s.category, s.productName")
        List<SalesProfitDto> findDatewiseProfit(String username, LocalDate startDate, LocalDate endDate);

        @Query("SELECT new com.example.bake_boss_backend.dto.TopSalesDTO(ss.productName, SUM(ss.saleRate * ss.productQty)) "
                        +
                        "FROM SalesStock ss " +
                        "WHERE ss.username = :username " +
                        "AND ss.status = 'sold' " +
                        "AND MONTH(ss.date) = MONTH(CURRENT_DATE) " +
                        "AND YEAR(ss.date) = YEAR(CURRENT_DATE) " +
                        "GROUP BY ss.productName " +
                        "ORDER BY SUM(ss.saleRate * ss.productQty) DESC")
        List<TopSalesDTO> findTop10SoldProductsByUsernameAndStatusSold(@Param("username") String username);

        @Query("SELECT new com.example.bake_boss_backend.dto.TopSalesDTO(ss.productName, SUM(ss.saleRate * ss.productQty)) " +
       "FROM SalesStock ss " +
       "WHERE ss.username = :username " +
       "AND ss.status = 'sold' " +
       "AND DATE(ss.date) = CURRENT_DATE " +
       "GROUP BY ss.productName " +
       "ORDER BY SUM(ss.saleRate * ss.productQty) DESC")
     List<TopSalesDTO> findTop10SoldProductsByUsernameAndStatusSoldForToday(@Param("username") String username);


        @Query("SELECT new com.example.bake_boss_backend.dto.SixMonthSaleDTO(MONTHNAME(s.date) as monthname, s.category as category, SUM(s.productQty * s.saleRate) as totalSale) " +
       "FROM SalesStock s " +
       "WHERE s.username = :username AND s.status = 'sold' " +
       "AND s.date >= :startDate " +
       "GROUP BY MONTH(s.date), s.category " +
       "ORDER BY MONTH(s.date) DESC")
List<SixMonthSaleDTO> findLastSixMonthsSalesByCategory(@Param("username") String username, @Param("startDate") LocalDate startDate);



@Query("SELECT new com.example.bake_boss_backend.dto.LossProfitAnalysis(MONTHNAME(s.date) as month, " +
       "SUM(CASE WHEN (s.saleRate > s.costPrice) THEN (s.productQty * (s.saleRate - s.costPrice)) ELSE 0 END) as profit, " +
       "SUM(CASE WHEN (s.saleRate < s.costPrice) THEN (s.productQty * (s.costPrice - s.saleRate)) ELSE 0 END) as loss) " +
       "FROM SalesStock s " +
       "WHERE s.username = :username AND s.status = 'sold' " +
       "AND s.date >= :startDate " +
       "GROUP BY MONTH(s.date) " +
       "ORDER BY MONTH(s.date) DESC")
List<LossProfitAnalysis> findLastTwelveMonthsProfitLoss(@Param("username") String username, @Param("startDate") LocalDate startDate);


}
