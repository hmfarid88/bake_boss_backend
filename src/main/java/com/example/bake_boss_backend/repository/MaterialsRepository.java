package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.DetailsSupplierDTO;
import com.example.bake_boss_backend.entity.MaterialsStock;

public interface MaterialsRepository extends JpaRepository<MaterialsStock, Long> {
    @Query("SELECT m FROM MaterialsStock m WHERE m.materialsName = :materialsName AND m.username = :username ORDER BY m.materialsId DESC LIMIT 1")
    Optional<MaterialsStock> findLatestByMaterialsNameAndUsername(@Param("materialsName") String materialsName,
            @Param("username") String username);

    @Query("SELECT ms FROM MaterialsStock ms WHERE ms.username=:username AND ms.materialsId IN " +
            "(SELECT MAX(ms2.materialsId) FROM MaterialsStock ms2 GROUP BY ms2.materialsName)")
    List<MaterialsStock> findLatestMaterialsStockByUsername(@Param("username") String username);

    @Query("SELECT ms FROM MaterialsStock ms WHERE status='stored' AND YEAR(ms.date) = :year AND MONTH(ms.date) = :month AND ms.username=:username")
    List<MaterialsStock> findMaterialsByUsername(@Param("year") int year, @Param("month") int month,
            @Param("username") String username);

    @Query("SELECT ms.supplierName, SUM(ms.materialsRate * ms.materialsQty) " +
            "FROM MaterialsStock ms WHERE ms.username = :username AND ms.status='stored' GROUP BY ms.supplierName")
    List<Object[]> findTotalMaterialCostGroupedBySupplierAndUsername(String username);

    @Query("SELECT new com.example.bake_boss_backend.dto.DetailsSupplierDTO(m.date, m.materialsName, SUM(m.materialsQty), SUM(m.materialsRate * m.materialsQty)) "
            +
            "FROM MaterialsStock m " +
            "WHERE m.username = :username AND m.supplierName = :supplierName AND m.date BETWEEN :startDate AND :endDate"
            +
            " GROUP BY m.date, m.materialsName")
    List<DetailsSupplierDTO> findMaterialsValueBySupplierAndUsername( String username, String supplierName,
            LocalDate startDate, LocalDate endDate);

    Optional<MaterialsStock> findByMaterialsId(Long materialsId);

    @Query("SELECT ms FROM MaterialsStock ms WHERE ms.username=:username AND ms.status='damaged'")
    List<MaterialsStock> findDamagedMaterialsByStatus(String username);
}
