package com.example.bake_boss_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailsDTO {
    private String category;
    private String productName;
    private String materialsName;
    private Double qty;
}
