package com.example.bake_boss_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRateDTO {
    private String productName;
    private Double unitRate;
    private Double saleRate;
    private Double qty;
}
