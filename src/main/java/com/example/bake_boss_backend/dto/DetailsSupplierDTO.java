package com.example.bake_boss_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsSupplierDTO {
    private LocalDate date;
    private String productName;
    private Double totalQty;
    private Double totalValue;

}
