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
@Table(name = "sales_stock", indexes = {
    @Index(name = "idx_product_name", columnList = "productName"),
    @Index(name = "idx_username", columnList = "username")
})
public class SalesStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private LocalDate date;
    private String category;
    private String productName;
    private Double costPrice;
    private Double saleRate;
    private Double discount;
    private Double productQty;
    private Double remainingQty;
    private String status;
    private String username;
    private String invoiceNo;
    private String soldInvoice;
}
