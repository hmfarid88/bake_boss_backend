package com.example.bake_boss_backend.dto;

import java.util.List;

import com.example.bake_boss_backend.entity.CustomerInfo;
import com.example.bake_boss_backend.entity.SalesStock;

import lombok.Data;

@Data
public class SalesRequest {
    private List<SalesStock> salesItems;
    private CustomerInfo customer;
}
