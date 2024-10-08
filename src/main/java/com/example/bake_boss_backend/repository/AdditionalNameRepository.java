package com.example.bake_boss_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bake_boss_backend.entity.AdditionalName;

public interface AdditionalNameRepository extends JpaRepository<AdditionalName, Long>{

    boolean existsByAdditionalName(String additionalName);

      
}
