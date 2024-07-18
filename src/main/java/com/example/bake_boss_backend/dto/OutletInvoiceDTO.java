package com.example.bake_boss_backend.dto;

import java.util.List;

import com.example.bake_boss_backend.entity.CustomerInfo;
import com.example.bake_boss_backend.entity.SalesStock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutletInvoiceDTO {
    private List<SalesStock> salesStock;
    private CustomerInfo customerInfo;

}
