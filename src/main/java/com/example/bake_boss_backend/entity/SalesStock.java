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
public class SalesStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String date;
    private String category;
    private String productName;
    private Double costPrice;
    private int productQty;
    private int remainingQty;
    private String status;
    private String username;
    private String invoiceNo;
}
