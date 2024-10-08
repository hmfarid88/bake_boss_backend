package com.example.bake_boss_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.ItemDetailsDTO;
import com.example.bake_boss_backend.entity.ItemMake;
import com.example.bake_boss_backend.entity.MaterialName;
import com.example.bake_boss_backend.entity.MaterialsStock;
import com.example.bake_boss_backend.entity.ProductRate;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.entity.SalesStock;
import com.example.bake_boss_backend.repository.ItemMakeRepository;
import com.example.bake_boss_backend.repository.MaterialsNameRepository;
import com.example.bake_boss_backend.repository.MaterialsRepository;
import com.example.bake_boss_backend.repository.ProductRateRepository;
import com.example.bake_boss_backend.repository.ProductStockrepository;
import com.example.bake_boss_backend.repository.SalesStockRepository;

import jakarta.transaction.Transactional;

@Service
public class ItemMakeService {
    @Autowired
    private ItemMakeRepository itemMakeRepository;

    @Autowired
    private MaterialsNameRepository materialNameRepository;

    @Autowired
    private MaterialsRepository materialsRepository;

    @Autowired
    private ProductStockrepository productStockrepository;

    @Autowired
    private SalesStockRepository salesStockrepository;

    @Autowired
    private ProductRateRepository productRateRepository;

    public List<Object[]> getMaterialsAndQtyGroupedByItemName(String username) {
        return itemMakeRepository.findMaterialsAndQtyGroupedByItemName(username);
    }

    @Transactional
    public void updateMaterialsName(String username, String oldMaterialsName, String newMaterialsName) {
        // Update MaterialName entity
        List<MaterialName> materialNames = materialNameRepository.findByUsernameAndMaterialsName(username,
                oldMaterialsName);
        if (materialNames != null) {
            for (MaterialName materialName : materialNames) {
                materialName.setMaterialsName(newMaterialsName);
                materialNameRepository.save(materialName);
            }
        }
        // Update ItemMake entity
        List<ItemMake> itemMakes = itemMakeRepository.findByUsernameAndMaterialsName(username, oldMaterialsName);
        if (itemMakes != null) {
            for (ItemMake itemMake : itemMakes) {
                itemMake.setMaterialsName(newMaterialsName);
                itemMakeRepository.save(itemMake);
            }
        }
        // Update MaterialsStock entity
        List<MaterialsStock> materialsStocks = materialsRepository.findByUsernameAndMaterialsName(username,
                oldMaterialsName);
        if (materialsStocks != null) {
            for (MaterialsStock materialsStock : materialsStocks) {
                materialsStock.setMaterialsName(newMaterialsName);
                materialsRepository.save(materialsStock);
            }
        }
    }

    @Transactional
    public void updateItemName(String username, String oldItemName, String newItemName) {
        // Update MaterialName entity
        List<ItemMake> itemNames = itemMakeRepository.findByUsernameAndItemName(username, oldItemName);
        if (itemNames != null) {
            for (ItemMake itemName : itemNames) {
                itemName.setItemName(newItemName);
                itemMakeRepository.save(itemName);
            }
        }
        List<ProductStock> productStocks = productStockrepository.findByUsernameAndProductName(username, oldItemName);
        if (productStocks != null) {
            for (ProductStock productStock : productStocks) {
                productStock.setProductName(newItemName);
                productStockrepository.save(productStock);
            }
        }

        List<SalesStock> salesStocks = salesStockrepository.findByProductName(oldItemName);
        if (salesStocks != null) {
            for (SalesStock salesStock : salesStocks) {
                salesStock.setProductName(newItemName);
                salesStockrepository.save(salesStock);
            }
        }

        List<ProductRate> productRates = productRateRepository.findByProductName(oldItemName);
        if (productRates != null) {
            for (ProductRate productRate : productRates) {
                productRate.setProductName(newItemName);
                productRateRepository.save(productRate);
            }
        }

    }
    public List<ItemDetailsDTO> findCategoryAndMaterialsByProductName(String productName) {
        return itemMakeRepository.findMaterialsNameAndQtyByItemName(productName);
        
    }

    public void deleteByUsernameAndItemName(String username, String itemName) {
        itemMakeRepository.deleteByUsernameAndItemName(username, itemName);
    }
    // public Map<String, Object> findCategoryAndMaterialsByProductName(String productName) {
    //     String category = productStockrepository.findCategoryByProductName(productName);
    //     List<ItemDetailsDTO> itemDetailsList = itemMakeRepository.findMaterialsNameAndQtyByItemName(productName);

    //     Map<String, Object> result = new HashMap<>();
    //     result.put("category", category);
    //     result.put("materials", itemDetailsList);

    //     return result;
    // }

    // public Map<String, Object> findCategoryAndMaterialsByProductName(String productName) {
    //     String category = productStockrepository.findCategoryByProductName(productName);
    //     ItemDetailsDTO itemDetails = itemMakeRepository.findMaterialsNameAndQtyByItemName(productName);
    //     Map<String, Object> result = new HashMap<>();
    //     result.put("category", category);
    //     result.put("materialsName", itemDetails.getMaterialsName());
    //     result.put("qty", itemDetails.getQty());
    //     return result;
    // }
}
