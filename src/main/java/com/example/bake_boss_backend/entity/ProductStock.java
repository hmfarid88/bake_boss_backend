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
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private LocalDate date;
    private String category;
    private String productName;
    private Double costPrice;
    private Double dpRate;
    private Double rpRate;
    private int productQty;
    private int remainingQty;
    private String status;
    private String customer;
    private String username;
    private String invoiceNo;

}
