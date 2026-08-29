package com.example.bake_boss_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.ProductRateDTO;
import com.example.bake_boss_backend.entity.ProductRate;

public interface ProductRateRepository extends JpaRepository<ProductRate, Long> {

    Optional<ProductRate> findTopByProductNameOrderByIdDesc(String productName);

    Optional<ProductRate> findByProductNameAndUsername(String productName, String username);

      @Query("""
        SELECT new com.example.bake_boss_backend.dto.ProductRateDTO(
            pr.productName,
            MAX(pr.unitRate),
            MAX(pr.saleRate),
            MAX(pr.qty)
        )
        FROM ProductRate pr
        GROUP BY pr.productName
        ORDER BY pr.productName ASC
    """)
    List<ProductRateDTO> findTopRatesByUsername();

    // List<ProductRate> findByProductName(String oldItemName);
}
