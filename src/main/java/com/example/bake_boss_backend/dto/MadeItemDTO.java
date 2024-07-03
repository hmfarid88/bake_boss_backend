package com.example.bake_boss_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MadeItemDTO {
    private String itemName;
    private String materialsName;
    private String itemNo;
    private Double qty;
    private Double averageRate;
    private Integer remainingQty;
    

}
