package com.example.bake_boss_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.entity.ProductionStock;
import com.example.bake_boss_backend.service.ProductionStockService;

@RestController
@RequestMapping("/production")
public class ProductionController {

    @Autowired
    private ProductionStockService productionService;

    @PostMapping("/addProductionMaterials")
    public List<ProductionStock> saveMaterials(@RequestBody List<ProductionStock> allItems) {
        return productionService.saveMaterials(allItems);
    }

     @GetMapping("/getProductionStock")
    public List<ProductionStock> getLatestProductionStock(@RequestParam String username) {
        return productionService.latestProductionStock(username);
    }

     @GetMapping("/getDatewiseProductionStock")
    public List<ProductionStock> getDatewiseProductionStock(@RequestParam String username, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return productionService.datewiseProductionStock(username, startDate, endDate);
    }
}
