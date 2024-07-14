package com.example.bake_boss_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesStockDTO {
    private Long productId;
    private String date;
    private String category;
    private String productName;
    private Double costPrice;
    private int remainingQty;
    private String invoiceNo;
    private int saleRate;
}
