package com.example.bake_boss_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesStockDTO {
    private Long productId;
    private LocalDate date;
    private String category;
    private String productName;
    private Double costPrice;
    private Double remainingQty;
    private String invoiceNo;
    private Double saleRate;
}
