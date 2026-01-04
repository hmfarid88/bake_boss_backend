package com.example.bake_boss_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaleReportDTO {
    private Long productId;
    private LocalDate date;
    private LocalTime time;
    private String category;
    private String productName;
    private String soldInvoice;
    private String customerName;
    private String soldBy;
    private String phoneNumber;
    private Double saleRate;
    private Double discount;
    private Double productQty;
}
