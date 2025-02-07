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
import com.example.bake_boss_backend.dto.SaleReportDTO;
import com.example.bake_boss_backend.dto.SalesProfitDto;
import com.example.bake_boss_backend.dto.SixMonthSaleDTO;
import com.example.bake_boss_backend.dto.StockLedgerDTO;
import com.example.bake_boss_backend.dto.SupplierSalesStockDTO;
import com.example.bake_boss_backend.entity.SalesStock;

public interface SalesStockRepository extends JpaRepository<SalesStock, Long> {
  @Query("SELECT ss FROM SalesStock ss WHERE ss.productName = :productName AND ss.username = :username ORDER BY ss.productId DESC LIMIT 1")
  Optional<SalesStock> findLatestSalesStockByProductNameAndUsername(@Param("productName") String productName,
      @Param("username") String username);

  @Query("SELECT s FROM SalesStock s WHERE s.username = :username AND s.productId IN (SELECT MAX(ss.productId) FROM SalesStock ss WHERE ss.username = :username GROUP BY ss.productName) ORDER BY s.productName ASC")
  List<SalesStock> findLastByProductNameAndUsername(@Param("username") String username);

  @Query("SELECT new com.example.bake_boss_backend.dto.SaleReportDTO(s.productId, s.date, s.time, s.category, s.productName, s.soldInvoice, c.customerName, c.phoneNumber, s.saleRate, s.discount, s.productQty) FROM SalesStock s JOIN CustomerInfo c ON s.soldInvoice = c.soldInvoice WHERE s.status = 'sold' AND MONTH(s.date) = MONTH(CURRENT_DATE) AND YEAR(s.date) = YEAR(CURRENT_DATE) AND s.username = :username")
  List<SaleReportDTO> findCurrentMonthSoldStocksByUsername(String username);
  
  @Query("SELECT s FROM SalesStock s WHERE s.status = 'vendor' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
  List<SalesStock> findCurrentMonthVendorSaleByUsername(@Param("username") String username);

  @Query("SELECT s FROM SalesStock s WHERE s.status = 'vendor' AND s.username = :username AND  s.date BETWEEN :startDate AND :endDate")
  List<SalesStock> findDatewiseVendorSaleByUsername(String username, LocalDate startDate, LocalDate endDate);

  @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND s.username = :username AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
  List<SalesStock> findCurrentMonthReturnedStocksByUsername(@Param("username") String username);

  @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND s.username = :username AND s.date BETWEEN :startDate AND :endDate")
  List<SalesStock> findDatewiseReturnedStocksByUsername(String username, LocalDate startDate, LocalDate endDate);

  @Query("SELECT new com.example.bake_boss_backend.dto.SaleReportDTO(s.productId, s.date, s.time, s.category, s.productName, s.soldInvoice, c.customerName, c.phoneNumber, s.saleRate, s.discount, s.productQty) FROM SalesStock s JOIN CustomerInfo c ON s.soldInvoice = c.soldInvoice WHERE s.status = 'sold' AND s.username = :username AND  s.date BETWEEN :startDate AND :endDate")
  List<SaleReportDTO> findDatewiseSoldStocksByUsername(String username, LocalDate startDate, LocalDate endDate);
 
  List<SalesStock> findByProductIdAndUsername(Long productId, String username);

  List<SalesStock> findBySoldInvoice(String soldInvoice);

  @Query("SELECT s.date, s.soldInvoice, SUM((s.productQty * s.saleRate) - COALESCE(s.discount, 0)) " +
      "FROM SalesStock s WHERE s.username = :username AND s.date = :date AND s.status = :status GROUP BY s.date, s.soldInvoice")
  List<Object[]> findByUsernameAndDateAndStatus(String username, LocalDate date, String status);

  @Query("SELECT ps FROM SalesStock ps WHERE ps.productName = :productName AND ps.username = :username ORDER BY ps.productId DESC LIMIT 1")
  Optional<SalesStock> findLatestProductStockByProductNameAndUsername(String productName, String username);

  @Query("SELECT NEW com.example.bake_boss_backend.dto.StockLedgerDTO( " +
      "s.date, s.time, COALESCE(ps.username, ci.customerName, 'Aurora') AS supplier, " +
      "COALESCE(s.invoiceNo, s.soldInvoice), s.category, s.productName, s.costPrice, s.status, s.productQty, s.remainingQty) "
      +
      "FROM SalesStock s " +
      "LEFT JOIN ProductStock ps ON s.invoiceNo = ps.invoiceNo " +
      "LEFT JOIN CustomerInfo ci ON s.soldInvoice = ci.soldInvoice " +
      "WHERE MONTH(s.date) = MONTH(CURRENT_DATE) AND YEAR(s.date) = YEAR(CURRENT_DATE) " +
      "AND s.username = :username ORDER BY s.date")
  List<StockLedgerDTO> findCurrentMonthDataByUsername(@Param("username") String username);

  @Query("SELECT NEW com.example.bake_boss_backend.dto.StockLedgerDTO( " +
      "s.date, s.time, COALESCE(ps.username, ci.customerName, 'Aurora') AS supplier, " +
      "COALESCE(s.invoiceNo, s.soldInvoice), s.category, s.productName, s.costPrice, s.status, s.productQty, s.remainingQty) "
      +
      "FROM SalesStock s " +
      "LEFT JOIN ProductStock ps ON s.invoiceNo = ps.invoiceNo " +
      "LEFT JOIN CustomerInfo ci ON s.soldInvoice = ci.soldInvoice " +
      "WHERE s.username = :username AND  s.date BETWEEN :startDate AND :endDate ORDER BY s.date")
  List<StockLedgerDTO> findDatewiseStockLedgerUsername(String username, LocalDate startDate, LocalDate endDate);

  @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND FUNCTION('MONTH', s.date) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE)")
  List<SalesStock> findCurrentMonthAllReturnedStocks();

  @Query("SELECT s FROM SalesStock s WHERE s.status = 'Returned' AND s.date BETWEEN :startDate AND :endDate")
  List<SalesStock> findDatewiseReturnedStocks(LocalDate startDate, LocalDate endDate);

  List<SalesStock> findByProductName(String oldItemName);

  @Query("SELECT SUM(s.productQty * s.saleRate) - SUM(COALESCE(s.discount, 0)) " +
      "FROM SalesStock s WHERE s.status = 'sold' AND s.username = :username AND s.date < :date")
  Double findTotalSaleRateByUsernameAndDateBefore(@Param("username") String username, @Param("date") LocalDate date);

  @Query("SELECT new com.example.bake_boss_backend.dto.SaleReportDTO(s.productId, s.date, s.time, s.category, s.productName, s.soldInvoice, c.customerName, c.phoneNumber, s.saleRate, s.discount, s.productQty) FROM SalesStock s JOIN CustomerInfo c ON s.soldInvoice = c.soldInvoice WHERE s.status = 'sold' AND s.date=CURRENT_DATE AND s.username = :username")
  List<SaleReportDTO> findTodaysSaleByUsername(String username);

  @Query("SELECT new com.example.bake_boss_backend.dto.PendingVendorDto(s.username, s.soldInvoice, SUM(s.productQty)) "
      +
      "FROM SalesStock s JOIN CustomerInfo c ON s.soldInvoice = c.soldInvoice " +
      "LEFT JOIN SalesStock ss ON c.soldInvoice = ss.invoiceNo " +
      "WHERE s.status = 'vendor' AND c.customerName = :customerName AND ss.invoiceNo IS NULL " +
      "GROUP BY s.username, s.soldInvoice")
  List<PendingVendorDto> findPendingVendorData(@Param("customerName") String customerName);

  @Query("SELECT ss FROM SalesStock ss WHERE ss.soldInvoice=:soldInvoice AND ss.soldInvoice NOT IN (SELECT s.invoiceNo FROM SalesStock s WHERE s.status='stored')")
  List<SalesStock> findBySoldInvoiceNotInStock(String soldInvoice);

  @Query("SELECT DISTINCT NEW com.example.bake_boss_backend.dto.SupplierSalesStockDTO( " +
      "s.date, s.time, COALESCE(ps.username, ss.username) AS supplier, " +
      "s.category, s.productName, s.invoiceNo, s.costPrice, s.productQty) " +
      "FROM SalesStock s " +
      "LEFT JOIN ProductStock ps ON s.invoiceNo = ps.invoiceNo " +
      "LEFT JOIN SalesStock ss ON s.invoiceNo = ss.soldInvoice AND ss.status = 'vendor' " +
      "WHERE s.status = 'stored' " +
      "AND MONTH(s.date) = MONTH(CURRENT_DATE) AND YEAR(s.date) = YEAR(CURRENT_DATE) " +
      "AND s.username = :username ")
  List<SupplierSalesStockDTO> findCurrentMonthEntryByUsername(@Param("username") String username);

  @Query("SELECT DISTINCT NEW com.example.bake_boss_backend.dto.SupplierSalesStockDTO( " +
      "s.date, s.time, COALESCE(ps.username, ss.username) AS supplier, " +
      "s.category, s.productName, s.invoiceNo, s.costPrice, s.productQty) " +
      "FROM SalesStock s " +
      "LEFT JOIN ProductStock ps ON s.invoiceNo = ps.invoiceNo " +
      "LEFT JOIN SalesStock ss ON s.invoiceNo = ss.soldInvoice AND ss.status = 'vendor' " +
      "WHERE s.status = 'stored' " +
      "AND s.date BETWEEN :startDate AND :endDate " +
      "AND s.username = :username ")
  List<SupplierSalesStockDTO> findDatewiseEntryByUsername(LocalDate startDate, LocalDate endDate, String username);

  @Modifying
  @Query("UPDATE SalesStock s SET s.remainingQty = s.remainingQty - :qtyDifference " +
      "WHERE s.productName = :productName AND s.username = :username AND s.productId >= :productId")
  void reduceRemainingQty(@Param("productName") String productName, @Param("username") String username,
      @Param("productId") Long productId, @Param("qtyDifference") Double qtyDifference);

  @Modifying
  @Query("UPDATE SalesStock s SET s.remainingQty = s.remainingQty + :qtyDifference " +
      "WHERE s.productName = :productName AND s.username = :username AND s.productId >= :productId")
  void increaseRemainingQty(@Param("productName") String productName, @Param("username") String username,
      @Param("productId") Long productId, @Param("qtyDifference") Double qtyDifference);

  List<SalesStock> findByUsernameAndProductNameAndProductIdGreaterThan(String username, String productName,
      Long productId);

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

  @Query("SELECT new com.example.bake_boss_backend.dto.SixMonthSaleDTO(MONTHNAME(s.date) as monthname, s.category as category, SUM(s.productQty * s.saleRate) as totalSale) "
      +
      "FROM SalesStock s " +
      "WHERE s.username = :username AND s.status = 'sold' " +
      "AND s.date >= :startDate " +
      "GROUP BY MONTHNAME(s.date), s.category ")
  List<SixMonthSaleDTO> findLastSixMonthsSalesByCategory(@Param("username") String username,
      @Param("startDate") LocalDate startDate);

  @Query("SELECT new com.example.bake_boss_backend.dto.LossProfitAnalysis(MONTHNAME(s.date) as month, " +
      "SUM(CASE WHEN (s.saleRate > s.costPrice) THEN (s.productQty * (s.saleRate - s.costPrice)) ELSE 0 END) as profit, "
      +
      "SUM(CASE WHEN (s.saleRate < s.costPrice) THEN (s.productQty * (s.costPrice - s.saleRate)) ELSE 0 END) as loss) "
      +
      "FROM SalesStock s " +
      "WHERE s.username = :username AND s.status = 'sold' " +
      "AND s.date >= :startDate " +
      "GROUP BY MONTHNAME(s.date) ")
  List<LossProfitAnalysis> findLastTwelveMonthsProfitLoss(@Param("username") String username,
      @Param("startDate") LocalDate startDate);

}
