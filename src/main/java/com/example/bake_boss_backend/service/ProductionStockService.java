package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.MaterialsStock;
import com.example.bake_boss_backend.entity.ProductionStock;
import com.example.bake_boss_backend.entity.RawMaterialStock;
import com.example.bake_boss_backend.repository.MaterialsRepository;
import com.example.bake_boss_backend.repository.ProductionStockRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductionStockService {

    @Autowired
    private ProductionStockRepository productionStockRepository;

    @Autowired
    private MaterialsRepository materialRepository;

    // public List<ProductionStock> saveMaterials(List<ProductionStock> allItems) {
    // for (ProductionStock newItem : allItems) {
    // Optional<ProductionStock> existingMaterialOpt = productionStockRepository
    // .findLatestByMaterialsNameAndUsername(newItem.getMaterialsName(),
    // newItem.getUsername());

    // if (existingMaterialOpt.isPresent()) {
    // ProductionStock existingMaterial = existingMaterialOpt.get();
    // Double newTotalQty = existingMaterial.getRemainingQty() +
    // newItem.getMaterialsQty();
    // newItem.setRemainingQty(newTotalQty);
    // } else {
    // newItem.setRemainingQty(newItem.getMaterialsQty());
    // }

    // // Save the new or updated ProductionStock
    // productionStockRepository.save(newItem);
    // }

    // // return productionStockRepository.findAll();
    // return allItems;
    // }

    @Transactional
    public List<ProductionStock> saveMaterials(List<ProductionStock> allItems) {

        for (ProductionStock newItem : allItems) {

            // Update Materials Stock
            Optional<MaterialsStock> latestStockOpt = materialRepository.findLatestByMaterialsNameAndUsername(
                    newItem.getMaterialsName(),
                    newItem.getUsername());

            double previousRemaining = latestStockOpt
                    .map(MaterialsStock::getRemainingQty)
                    .orElse(0.0);

            MaterialsStock stock = new MaterialsStock();
            stock.setDate(LocalDate.now());
            stock.setMaterialsName(newItem.getMaterialsName());
            stock.setUsername(newItem.getUsername());
            stock.setStatus("Production");
            stock.setMadeItem(newItem.getMadeItem());
            stock.setMaterialsQty(newItem.getMaterialsQty());
            stock.setAverageRate(latestStockOpt.map(MaterialsStock::getAverageRate).orElse(0.0));
            stock.setMaterialsRate(latestStockOpt.map(MaterialsStock::getMaterialsRate).orElse(0.0));
            stock.setRemainingQty(previousRemaining - newItem.getMaterialsQty());
            
            materialRepository.save(stock);

            // Update Production Stock
            Optional<ProductionStock> existingMaterialOpt = productionStockRepository
                    .findLatestByMaterialsNameAndUsername(
                            newItem.getMaterialsName(),
                            newItem.getUsername());

            double newRemainingQty = existingMaterialOpt
                    .map(ps -> ps.getRemainingQty() + newItem.getMaterialsQty())
                    .orElse(newItem.getMaterialsQty());

            newItem.setRemainingQty(newRemainingQty);

            productionStockRepository.save(newItem);
        }

        return allItems;
    }

    public List<ProductionStock> latestProductionStock(String username) {
        return productionStockRepository.findLatestMaterialsForEachMaterialsName(username);
    }

    public List<ProductionStock> datewiseProductionStock(String username, LocalDate startDate, LocalDate endDate) {
        return productionStockRepository.findDatewisProductionStocksByUsername(username, startDate, endDate);
    }
}
