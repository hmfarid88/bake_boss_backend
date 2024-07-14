package com.example.bake_boss_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bake_boss_backend.entity.MarginSetup;

public interface MarginSetupRepository extends JpaRepository<MarginSetup, Integer> {
   
    MarginSetup findByUsername(String username);
}
