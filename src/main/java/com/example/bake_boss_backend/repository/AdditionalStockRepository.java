package com.example.bake_boss_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bake_boss_backend.entity.AdditionalStock;

public interface AdditionalStockRepository extends JpaRepository<AdditionalStock, Long> {
    Optional<AdditionalStock> findByProductNameAndUsername(String productName, String username);

    List<AdditionalStock> findAllByUsername(String username);

    Optional<AdditionalStock> findByIdAndUsername(Long id, String username);
}
