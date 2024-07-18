package com.example.bake_boss_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bake_boss_backend.entity.ProductRate;

public interface ProductRateRepository extends JpaRepository<ProductRate, Long> {

    ProductRate findByUsername(String username);

    Optional<ProductRate> findByProductNameAndUsername(String productName, String username);
}
