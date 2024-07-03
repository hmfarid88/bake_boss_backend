package com.example.bake_boss_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bake_boss_backend.entity.MaterialName;

public interface MaterialsNameRepository extends JpaRepository<MaterialName, Long>{

    boolean existsByUsernameAndMaterialsName(String username, String materialsName);

    List<MaterialName> getMaterialsNameByUsername(String username);
    
}
