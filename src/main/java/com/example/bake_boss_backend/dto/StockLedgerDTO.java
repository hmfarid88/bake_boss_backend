package com.example.bake_boss_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockLedgerDTO {
    private LocalDate date;
    private LocalTime time;
    private String supplier;
    private String invoiceNo;
    private String category;
    private String productName;
    private Double costPrice;
    private String status;
    private Double productQty;
    private Double remainingQty;
}
