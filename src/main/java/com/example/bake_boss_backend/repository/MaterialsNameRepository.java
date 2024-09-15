package com.example.bake_boss_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bake_boss_backend.entity.MaterialName;

import jakarta.transaction.Transactional;

public interface MaterialsNameRepository extends JpaRepository<MaterialName, Long> {

    boolean existsByUsernameAndMaterialsName(String username, String materialsName);

    List<MaterialName> getMaterialsNameByUsername(String username);

    List<MaterialName> findByUsernameAndMaterialsName(String username, String materialsName);

    @Transactional
    void deleteByUsernameAndMaterialsName(String username, String materialsName);
}
