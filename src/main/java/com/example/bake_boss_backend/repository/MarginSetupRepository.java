package com.example.bake_boss_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bake_boss_backend.entity.MarginSetup;

public interface MarginSetupRepository extends JpaRepository<MarginSetup, Integer> {
   
    Optional<MarginSetup> findByUsernameAndProductName(String username, String productName);

}
