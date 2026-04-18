package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.entity.RawMaterialStock;

import jakarta.transaction.Transactional;

public interface RawMaterialRepository extends JpaRepository<RawMaterialStock, Long> {
    @Query(value = "SELECT * FROM raw_material_stock m " +
            "WHERE m.materials_name = :materialsName AND m.username = :username " +
            "ORDER BY m.materials_id DESC LIMIT 1", nativeQuery = true)
    Optional<RawMaterialStock> findLatestByRawMaterialsNameAndUsername(@Param("materialsName") String materialsName,
            @Param("username") String username);

    @Query("SELECT ms FROM RawMaterialStock ms WHERE ms.materialsId IN " +
            "(SELECT MAX(ms2.materialsId) FROM RawMaterialStock ms2 GROUP BY ms2.materialsName)")
    List<RawMaterialStock> findLatestRawMaterialsStock();

    @Modifying
    @Transactional
    @Query("""
                UPDATE RawMaterialStock m
                SET m.averageRate = :averageRate
                WHERE m.materialsId = :materialsId
            """)
    int updateAverageRateByMaterialsId(
            @Param("materialsId") Long materialsId,
            @Param("averageRate") Double averageRate);

    @Modifying
    @Transactional
    @Query("""
                UPDATE RawMaterialStock m
                SET m.remainingQty = :materialsQty
                WHERE m.materialsId = :materialsId
            """)
    int updateMaterialsQtyByMaterialsId(
            @Param("materialsId") Long materialsId,
            @Param("materialsQty") Double materialsQty);

    Optional<RawMaterialStock> findByMaterialsId(Long materialsId);

     @Query("SELECT ps FROM RawMaterialStock ps WHERE ps.materialsName = :materialsName AND ps.username = :username ORDER BY ps.materialsId DESC LIMIT 1")
    Optional<RawMaterialStock> findLatestByMaterialsNameAndUsername(String materialsName, String username);

    List<RawMaterialStock> findBySupplierInvoice(String invoiceNo);

    @Query("SELECT ms FROM RawMaterialStock ms WHERE YEAR(ms.date) = :year AND MONTH(ms.date) = :month AND ms.username=:username")
    List<RawMaterialStock> findMaterialsByUsername(@Param("year") int year, @Param("month") int month, @Param("username") String username);

     @Query("SELECT ms FROM RawMaterialStock ms WHERE ms.username=:username AND ms.date BETWEEN :startDate AND :endDate")
    List<RawMaterialStock> findDatewiseRawMaterialsByUsername(String username, LocalDate startDate, LocalDate endDate);

    @Query("SELECT ms FROM RawMaterialStock ms WHERE status='stored' AND YEAR(ms.date) = :year AND MONTH(ms.date) = :month AND ms.username=:username")
     List<RawMaterialStock> findStoredRawMaterialsByUsername(@Param("year") int year, @Param("month") int month, @Param("username") String username);
        
     @Query("SELECT ms FROM RawMaterialStock ms WHERE status='sold' AND YEAR(ms.date) = :year AND MONTH(ms.date) = :month AND ms.username=:username")
        List<RawMaterialStock> findSoldRawMaterialsByUsername(@Param("year") int year, @Param("month") int month, @Param("username") String username);

     @Query("SELECT ms FROM RawMaterialStock ms WHERE status='stored' AND ms.username=:username AND ms.date BETWEEN :startDate AND :endDate")
    List<RawMaterialStock> findDatewiseStoredRawMaterialsByUsername(String username, LocalDate startDate, LocalDate endDate);
     
    @Query("SELECT ms FROM RawMaterialStock ms WHERE status='sold' AND ms.username=:username AND ms.date BETWEEN :startDate AND :endDate")
    List<RawMaterialStock> findDatewiseSoldRawMaterialsByUsername(String username, LocalDate startDate, LocalDate endDate);

}
