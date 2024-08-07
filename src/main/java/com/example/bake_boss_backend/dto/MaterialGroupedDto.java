package com.example.bake_boss_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialGroupedDto {
    private String itemName;
    private String materialsName;
    private Double qty;
    private Double averageRate;
}
