package com.example.bake_boss_backend.entity;

import java.time.LocalDate;

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
@Table(name = "materials_stock", indexes = {
        @Index(name = "idx_supplier_name", columnList = "supplierName"),
        @Index(name = "idx_supplier_invoice", columnList = "supplierInvoice"),
        @Index(name = "idx_materials_name", columnList = "materialsName"),
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_made_item", columnList = "madeItem")
})
public class MaterialsStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialsId;
    private LocalDate date;
    private String supplierName;
    private String supplierInvoice;
    private String materialsName;
    private String username;
    private String status;
    private String madeItem;
    private Double materialsRate;
    private Double averageRate;
    private Double materialsQty;
    private Double remainingQty;

}
