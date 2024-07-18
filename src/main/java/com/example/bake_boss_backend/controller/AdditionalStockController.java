package com.example.bake_boss_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.entity.AdditionalStock;
import com.example.bake_boss_backend.service.AdditionalStockService;

@RestController
@RequestMapping("/additionalStock")
public class AdditionalStockController {
    @Autowired
    private AdditionalStockService additionalStockService;

    @PutMapping("/addOrUpdate")
    public ResponseEntity<String> addOrUpdateStock(@RequestBody AdditionalStock stock) {
        additionalStockService.addOrUpdateStock(stock);
        return ResponseEntity.ok("Stock added or updated successfully");
    }

     @GetMapping("/getAdditionalProducts")
    public ResponseEntity<List<AdditionalStock>> findAllByUsername(@RequestParam String username) {
        List<AdditionalStock> stocks = additionalStockService.findAllByUsername(username);
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/getAdditionalByIdAndUsername")
    public ResponseEntity<AdditionalStock> findByIdAndUsername(@RequestParam Long id, @RequestParam String username) {
        Optional<AdditionalStock> stock = additionalStockService.findByIdAndUsername(id, username);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
