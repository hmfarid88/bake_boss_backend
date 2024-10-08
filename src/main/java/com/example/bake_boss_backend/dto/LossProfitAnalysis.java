package com.example.bake_boss_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LossProfitAnalysis {
    private String month;
    private Double profit;
    private Double loss;
}
