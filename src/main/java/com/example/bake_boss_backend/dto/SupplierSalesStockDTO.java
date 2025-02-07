package com.example.bake_boss_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class SupplierSalesStockDTO {
    private LocalDate date;
    private LocalTime time;
    private String supplier;
    private String category;
    private String productName;
    private String invoiceNo;
    private Double costPrice;
    private Double productQty;
   
}
