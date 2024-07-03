package com.example.bake_boss_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialsStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialsId;
    private String date;
    private String supplierName;
    private String supplierInvoice;
    private String materialsName;
    private String username;
    private String status;
    private String madeItem;
    private Double materialsRate;
    private Double averageRate;
    private int materialsQty;
    private int remainingQty;

}