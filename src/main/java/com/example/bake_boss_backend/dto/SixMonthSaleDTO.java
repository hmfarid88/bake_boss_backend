package com.example.bake_boss_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SixMonthSaleDTO {
    private String monthname;
    private String category;
    private Double totalSale;

}
