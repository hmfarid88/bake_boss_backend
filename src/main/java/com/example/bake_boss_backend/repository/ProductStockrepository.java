package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.DistProductDto;
import com.example.bake_boss_backend.dto.EditInvoiceDto;
import com.example.bake_boss_backend.dto.PendingStockDto;
import com.example.bake_boss_backend.entity.ProductStock;

import jakarta.transaction.Transactional;

public interface ProductStockrepository extends JpaRepository<ProductStock, Long> {
        @Query("SELECT ps FROM ProductStock ps WHERE ps.productName = :productName AND ps.username = :username ORDER BY ps.productId DESC LIMIT 1")
        Optional<ProductStock> findLatestProductStockByProductNameAndUsername(@Param("productName") String productName, @Param("username") String username);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.username=:username AND ps.productId IN " +
                        "(SELECT MAX(ps2.productId) FROM ProductStock ps2 GROUP BY ps2.productName)")
        List<ProductStock> findLatestProductStockForEachProductName(@Param("username") String username);

        Optional<ProductStock> findByProductId(Long productId);

        List<ProductStock> findByUsernameAndInvoiceNo(String username, String invoiceNo);


@Query("SELECT new com.example.bake_boss_backend.dto.DistProductDto(" +
       "ps.date, ps.time, ps.customer, ps.category, ps.productName, ps.invoiceNo, ps.dpRate, ps.costPrice, ps.productQty, MAX(pr.saleRate)) " +
       "FROM ProductStock ps " +
       "JOIN ProductRate pr ON ps.productName = pr.productName " +
       "WHERE ps.status = 'sold' " +
       "AND FUNCTION('YEAR', ps.date) = :year " +
       "AND FUNCTION('MONTH', ps.date) = :month " +
       "AND ps.username = :username " +
       "GROUP BY ps.date, ps.time, ps.customer, ps.category, ps.productName, ps.invoiceNo, ps.dpRate, ps.costPrice, ps.productQty")
        List<DistProductDto> findSoldProductsWithSaleRate(
        @Param("year") int year,
        @Param("month") int month,
        @Param("username") String username);



        @Query("SELECT ps FROM ProductStock ps WHERE ps.status='sold' AND YEAR(ps.date) = :year AND MONTH(ps.date) = :month AND ps.username=:username")
        List<ProductStock> findProductByStatus(@Param("year") int year, @Param("month") int month, @Param("username") String username);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.status='sold' AND ps.username=:username AND ps.date BETWEEN :startDate AND :endDate")
        List<ProductStock> findDatewiseProductByStatus(String username, LocalDate startDate, LocalDate endDate);

      
        @Query("SELECT new com.example.bake_boss_backend.dto.DistProductDto(" +
       "ps.date, ps.time, ps.customer, ps.category, ps.productName, ps.invoiceNo, ps.dpRate, ps.costPrice, ps.productQty, MAX(pr.saleRate)) " +
       "FROM ProductStock ps " +
       "JOIN ProductRate pr ON ps.productName = pr.productName " +
       "WHERE ps.status = 'sold' " +
       "AND ps.username = :username " +
       "AND ps.date BETWEEN :startDate AND :endDate " +
       "GROUP BY ps.date, ps.time, ps.customer, ps.category, ps.productName, ps.invoiceNo, ps.dpRate, ps.costPrice, ps.productQty")
        List<DistProductDto> datewiseSoldByUsername(String username, LocalDate startDate, LocalDate endDate);

        @Query("SELECT new com.example.bake_boss_backend.dto.PendingStockDto(ps.invoiceNo, SUM(ps.productQty)) " +
                        "FROM ProductStock ps " +
                        "WHERE ps.customer=:customer " +
                        "AND ps.invoiceNo NOT IN (SELECT ss.invoiceNo FROM SalesStock ss WHERE ss.status='stored') " +
                        "GROUP BY ps.invoiceNo")
        List<PendingStockDto> findProductStockWithInvoiceNotInSalesStock(String customer);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.username=:username AND ps.status='damaged'")
        List<ProductStock> findDamagedProductByStatus(String username);

        @Query("SELECT ps FROM ProductStock ps WHERE  YEAR(ps.date) = :year AND MONTH(ps.date) = :month AND ps.username=:username")
        List<ProductStock> findProductByUsername(@Param("year") int year, @Param("month") int month, @Param("username") String username);

        @Query("SELECT ps FROM ProductStock ps WHERE  ps.username=:username AND ps.date BETWEEN :startDate AND :endDate")
        List<ProductStock> findDatewiseProductByUsername(String username, LocalDate startDate, LocalDate endDate);

        List<ProductStock> findByUsernameAndProductName(String username, String oldItemName);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.customer=:customer AND ps.invoiceNo=:invoiceNo AND ps.invoiceNo NOT IN (SELECT ss.invoiceNo FROM SalesStock ss WHERE ss.status='stored')")
        List<ProductStock> findByCustomerAndInvoiceNo(String customer, String invoiceNo);

        @Query("SELECT new com.example.bake_boss_backend.dto.EditInvoiceDto(p.invoiceNo, p.customer) FROM ProductStock p WHERE p.status = 'sold' AND p.username = :username AND p.invoiceNo NOT IN (SELECT s.invoiceNo FROM SalesStock s WHERE s.status = 'stored') GROUP BY p.invoiceNo, p.customer")
        List<EditInvoiceDto> findSoldProductsNotInSalesStock(@Param("username") String username);
       
        List<ProductStock> findByInvoiceNo(String invoiceNo);

        @Modifying
        @Transactional
        @Query("UPDATE ProductStock p SET p.customer = :customer WHERE p.invoiceNo = :invoiceNo")
        int updateCustomerByInvoiceNo(String customer, String invoiceNo);

        @Modifying
        @Transactional
        @Query("UPDATE ProductStock p SET p.productQty = :productQty WHERE p.productId = :productId")
        int updateProductQtyByProductId(Double productQty, Long productId);
}
