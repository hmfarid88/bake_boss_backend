package com.example.bake_boss_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.PendingStockDto;
import com.example.bake_boss_backend.entity.ProductStock;

public interface ProductStockrepository extends JpaRepository<ProductStock, Long> {
        @Query("SELECT ps FROM ProductStock ps WHERE ps.productName = :productName AND ps.username = :username ORDER BY ps.productId DESC LIMIT 1")
        Optional<ProductStock> findLatestProductStockByProductNameAndUsername(@Param("productName") String productName,
                        @Param("username") String username);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.username=:username AND ps.productId IN " +
                        "(SELECT MAX(ps2.productId) FROM ProductStock ps2 GROUP BY ps2.productName)")
        List<ProductStock> findLatestProductStockForEachProductName(@Param("username") String username);

        Optional<ProductStock> findByProductId(Long productId);

        List<ProductStock> findByUsernameAndInvoiceNo(String username, String invoiceNo);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.status='sold' AND YEAR(ps.date) = :year AND MONTH(ps.date) = :month AND ps.username=:username")
        List<ProductStock> findProductByStatus(@Param("year") int year, @Param("month") int month,
                        @Param("username") String username);

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

        List<ProductStock> findByUsernameAndProductName(String username, String oldItemName);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.customer=:customer AND ps.invoiceNo=:invoiceNo AND ps.invoiceNo NOT IN (SELECT ss.invoiceNo FROM SalesStock ss WHERE ss.status='stored')")
        List<ProductStock> findByCustomerAndInvoiceNo(String customer, String invoiceNo);
}
