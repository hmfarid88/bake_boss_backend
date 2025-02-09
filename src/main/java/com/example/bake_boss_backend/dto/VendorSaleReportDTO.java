package com.example.bake_boss_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VendorSaleReportDTO {
    private LocalDate date;
    private LocalTime time;
    private String category;
    private String productName;
    private String soldInvoice;
    private String customerName;
    private Double saleRate;
    private Double productQty;
}
