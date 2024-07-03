package com.example.bake_boss_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bake_boss_backend.dto.MadeItemDTO;
import com.example.bake_boss_backend.entity.ItemMake;

public interface ItemMakeRepository extends JpaRepository<ItemMake, Long> {
    @Query("SELECT i.itemName FROM ItemMake i where i.username=:username GROUP BY i.itemName")
    List<String> findDistinctItems(String username);

    @Query("SELECT new com.example.bake_boss_backend.dto.MadeItemDTO(i.itemName, i.materialsName, i.itemNo, i.qty, ms.averageRate, ms.remainingQty) "
            +
            "FROM ItemMake i " +
            "JOIN MaterialsStock ms ON i.materialsName = ms.materialsName " +
            "WHERE i.username = :username AND i.itemName = :itemName " +
            "AND ms.materialsId = (SELECT MAX(ms2.materialsId) FROM MaterialsStock ms2 WHERE ms2.materialsName = ms.materialsName AND ms2.username = :username)")
    List<MadeItemDTO> findByUsernameGrouped(String username, String itemName);

    List<ItemMake> findByItemNo(String itemNo);
    List<ItemMake> findByItemName(String itemName);

    
    }
