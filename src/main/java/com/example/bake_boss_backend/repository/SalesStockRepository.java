package com.example.bake_boss_backend.repository;

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

    // List<SalesStock> findAllByUsername(String username);

    @Query("SELECT s FROM SalesStock s WHERE s.username = :username AND s.productId = (SELECT MAX(ss.productId) FROM SalesStock ss WHERE ss.productName = s.productName AND ss.username = :username)")
    List<SalesStock> findLastByProductNameAndUsername(@Param("username") String username);

   
    List<SalesStock> findByProductIdAndUsername(Long productId, String username);
}
