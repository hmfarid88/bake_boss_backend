package com.example.bake_boss_backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.SalesStockDTO;
import com.example.bake_boss_backend.service.SalesStockService;

@RestController
@RequestMapping("/sales")
public class SalesController {
    @Autowired
    private SalesStockService salesStockService;

   
    @GetMapping("/getSalesStock")
    public List<SalesStockDTO> getAllSalesStockWithRate(String username) {
        return salesStockService.getAllSalesStockWithRate(username);
    }

    @GetMapping("/getSingleProduct")
    public List<SalesStockDTO> getSingleProduct(Long productId, String username) {
        return salesStockService.getSingleSalesStockWithRate(productId, username);
    }
}
