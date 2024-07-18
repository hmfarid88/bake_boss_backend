package com.example.bake_boss_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.AdditionalStock;
import com.example.bake_boss_backend.repository.AdditionalStockRepository;

import jakarta.transaction.Transactional;

@Service
public class AdditionalStockService {
    @Autowired
    private AdditionalStockRepository additionalStockRepository;

    @Transactional
    public void addOrUpdateStock(AdditionalStock stock) {
        Optional<AdditionalStock> existingStock = additionalStockRepository
                .findByProductNameAndUsername(stock.getProductName(), stock.getUsername());

        if (existingStock.isPresent()) {
            AdditionalStock stockToUpdate = existingStock.get();
            stockToUpdate.setCostPrice((stock.getCostPrice())); 
            stockToUpdate.setSalePrice(stock.getSalePrice());
            additionalStockRepository.save(stockToUpdate);
        } else {
            additionalStockRepository.save(stock);
        }
    }

    public List<AdditionalStock> findAllByUsername(String username) {
        return additionalStockRepository.findAllByUsername(username);
    }

    public Optional<AdditionalStock> findByIdAndUsername(Long id, String username) {
        return additionalStockRepository.findByIdAndUsername(id, username);
    }
}
