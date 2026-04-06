package com.example.bake_boss_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FactoryInvoiceDTO {
    private LocalDate date;
    private LocalTime time;
    private String category;
    private String productName;
    private Double productQty;
    private String customerName;
    private String soldInvoice;
    private Double saleRate;

}
