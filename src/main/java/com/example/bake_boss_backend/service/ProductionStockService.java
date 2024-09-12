package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.ProductionStock;
import com.example.bake_boss_backend.repository.ProductionStockRepository;

@Service
public class ProductionStockService {

    @Autowired
    private ProductionStockRepository productionStockRepository;

    public List<ProductionStock> saveMaterials(List<ProductionStock> allItems) {
        for (ProductionStock newItem : allItems) {
            Optional<ProductionStock> existingMaterialOpt = productionStockRepository
                    .findLatestByMaterialsNameAndUsername(newItem.getMaterialsName(), newItem.getUsername());

            if (existingMaterialOpt.isPresent()) {
                ProductionStock existingMaterial = existingMaterialOpt.get();
                Double newTotalQty = existingMaterial.getRemainingQty() + newItem.getMaterialsQty();
                newItem.setRemainingQty(newTotalQty);
            } else {
                newItem.setRemainingQty(newItem.getMaterialsQty());
            }

            // Save the new or updated ProductionStock
            productionStockRepository.save(newItem);
        }

        return productionStockRepository.findAll();
    }

    public List<ProductionStock> latestProductionStock(String username) {
        return productionStockRepository.findLatestMaterialsForEachMaterialsName(username);
    }

    public List<ProductionStock> datewiseProductionStock(String username, LocalDate startDate, LocalDate endDate) {
        return productionStockRepository.findDatewisProductionStocksByUsername(username, startDate, endDate);
    }
}
