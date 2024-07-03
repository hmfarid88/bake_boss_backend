package com.example.bake_boss_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.entity.MaterialsStock;

public interface MaterialsRepository extends JpaRepository<MaterialsStock, Long> {
    @Query("SELECT m FROM MaterialsStock m WHERE m.materialsName = :materialsName AND m.username = :username ORDER BY m.materialsId DESC LIMIT 1")
    Optional<MaterialsStock> findLatestByMaterialsNameAndUsername(@Param("materialsName") String materialsName, @Param("username") String username);

    @Query("SELECT ms FROM MaterialsStock ms WHERE ms.username=:username AND ms.materialsId IN " +
            "(SELECT MAX(ms2.materialsId) FROM MaterialsStock ms2 GROUP BY ms2.materialsName)")
    List<MaterialsStock> findLatestMaterialsStockByUsername(@Param("username") String username);
}
