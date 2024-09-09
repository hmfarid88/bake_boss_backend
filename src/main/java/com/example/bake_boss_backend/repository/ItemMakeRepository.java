package com.example.bake_boss_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bake_boss_backend.dto.ItemDetailsDTO;
import com.example.bake_boss_backend.dto.MadeItemDTO;
import com.example.bake_boss_backend.entity.ItemMake;

public interface ItemMakeRepository extends JpaRepository<ItemMake, Long> {
        @Query("SELECT i.itemName FROM ItemMake i GROUP BY i.itemName")
        List<String> findDistinctItems();

        @Query("SELECT new com.example.bake_boss_backend.dto.MadeItemDTO(i.itemId, i.itemName, i.materialsName, i.itemNo, i.qty, ms.averageRate, ms.remainingQty) "
                        +
                        "FROM ItemMake i " +
                        "JOIN MaterialsStock ms ON i.materialsName = ms.materialsName " +
                        "WHERE i.username = :username AND i.itemName = :itemName " +
                        "AND ms.materialsId = (SELECT MAX(ms2.materialsId) FROM MaterialsStock ms2 WHERE ms2.materialsName = ms.materialsName AND ms2.username = :username)")
        List<MadeItemDTO> findByUsernameGrouped(String username, String itemName);

        @Query("SELECT im.itemName, im.materialsName, im.qty, ms.averageRate FROM ItemMake im JOIN MaterialsStock ms ON im.materialsName = ms.materialsName  WHERE im.username = :username GROUP BY im.itemName, im.materialsName, im.qty, ms.averageRate")
        List<Object[]> findMaterialsAndQtyGroupedByItemName(String username);

        @Query("SELECT new com.example.bake_boss_backend.dto.ItemDetailsDTO(ps.category, ps.productName, im.materialsName, im.qty) FROM ProductStock ps "
                        +
                        "JOIN ItemMake im ON ps.productName = im.itemName WHERE ps.productName = :productName GROUP BY ps.category, ps.productName, im.materialsName, im.qty")
        List<ItemDetailsDTO> findMaterialsNameAndQtyByItemName(String productName);

        List<ItemMake> findByItemNo(String itemNo);

        List<ItemMake> findByItemName(String itemName);

        List<ItemMake> findByItemNoAndMaterialsName(String itemNo, String materialsName);

        List<ItemMake> findByUsernameAndMaterialsName(String username, String materialsName);

        List<ItemMake> findByUsernameAndItemName(String username, String itemName);

}
