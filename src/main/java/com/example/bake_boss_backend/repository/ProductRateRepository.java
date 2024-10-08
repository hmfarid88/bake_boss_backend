package com.example.bake_boss_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bake_boss_backend.entity.ProductRate;

public interface ProductRateRepository extends JpaRepository<ProductRate, Long> {

    Optional<ProductRate> findByProductNameAndUsername(String productName, String username);

    List<ProductRate> findByProductName(String oldItemName);
}
