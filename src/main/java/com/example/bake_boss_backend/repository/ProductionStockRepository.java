package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.entity.ProductionStock;

public interface ProductionStockRepository extends JpaRepository<ProductionStock, Long> {

    @Query("SELECT ps FROM ProductionStock ps WHERE ps.materialsName = :materialsName AND ps.username = :username ORDER BY ps.materialsId DESC LIMIT 1")
    Optional<ProductionStock> findLatestByMaterialsNameAndUsername(String materialsName, String username);

    @Query("SELECT ps FROM ProductionStock ps WHERE ps.username=:username AND ps.materialsId IN " +
            "(SELECT MAX(ps2.materialsId) FROM ProductionStock ps2 GROUP BY ps2.materialsName)")
    List<ProductionStock> findLatestMaterialsForEachMaterialsName(@Param("username") String username);

    @Query("SELECT s FROM ProductionStock s WHERE s.username = :username AND  s.date BETWEEN :startDate AND :endDate")
    List<ProductionStock> findDatewisProductionStocksByUsername(String username, LocalDate startDate, LocalDate endDate);
            
}
