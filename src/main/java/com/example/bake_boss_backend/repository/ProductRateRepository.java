package com.example.bake_boss_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bake_boss_backend.entity.ProductRate;

public interface ProductRateRepository extends JpaRepository<ProductRate, Long> {

    ProductRate findByUsername(String username);

//  @Query("SELECT ps FROM ProductRate ps WHERE ps.productName = :productName AND ps.username = :username ORDER BY ps.productId DESC LIMIT 1")
    Optional<ProductRate> findByProductNameAndUsername(String productName, String username);
}
