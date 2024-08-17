package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.MaterialsStock;
import com.example.bake_boss_backend.entity.ProductRate;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.repository.MaterialsRepository;
import com.example.bake_boss_backend.repository.ProductRateRepository;
import com.example.bake_boss_backend.repository.ProductStockrepository;

import jakarta.transaction.Transactional;

@Service
public class ProductStockService {
    @Autowired
    private ProductStockrepository productStockRepository;

    @Autowired
    private ProductRateRepository productRateRepository;

    @Autowired
    private MaterialsRepository materialsRepository;

    public List<ProductStock> getProductStockWithInvoiceNotInSalesStock(String customer) {
        return productStockRepository.findProductStockWithInvoiceNotInSalesStock(customer);
    }

    @Transactional
    public ProductRate upsertProductRate(String username, String productName, Integer saleRate) {
        Optional<ProductRate> existingSetup = productRateRepository.findByProductNameAndUsername(productName, username);
        if (existingSetup.isPresent()) {
            ProductRate productRate = existingSetup.get();
            productRate.setSaleRate(saleRate);
            return productRateRepository.save(productRate);
        } else {
            ProductRate newProductRate = new ProductRate();
            newProductRate.setUsername(username);
            newProductRate.setProductName(productName);
            newProductRate.setSaleRate(saleRate);
            return productRateRepository.save(newProductRate);
        }
    }

    public List<ProductStock> getProductDistForCurrentMonth(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return productStockRepository.findProductByStatus(year, month, username);
    }

    public List<ProductStock> getAllProductStock(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return productStockRepository.findProductByUsername(year, month, username);
    }

    public List<MaterialsStock> getAllMaterialsStock(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return materialsRepository.findMaterialsByUsername(year, month, username);
    }
}
