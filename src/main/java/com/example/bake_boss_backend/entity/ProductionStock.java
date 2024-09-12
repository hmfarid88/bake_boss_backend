package com.example.bake_boss_backend.entity;

import java.time.LocalDate;

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
public class ProductionStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialsId;
    private LocalDate date;
    private String category;
    private String invoiceNo;
    private String materialsName;
    private String username;
    private String status;
    private String madeItem;
    private Double materialsQty;
    private Double remainingQty;
}
