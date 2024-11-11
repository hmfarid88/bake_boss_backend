package com.example.bake_boss_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
    @Index(name = "idx_item_name", columnList = "itemName"),
    @Index(name = "idx_materials_name", columnList = "materialsName"),
    @Index(name = "idx_username", columnList = "username"),
    @Index(name = "idx_username_materials_name", columnList = "username, materialsName")
})
public class ItemMake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String itemNo;
    private String itemName;
    private String materialsName;
    private Double qty;
    private String username;

}
