package com.example.bake_boss_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.MarginSetup;
import com.example.bake_boss_backend.entity.ProductRate;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.repository.ProductRateRepository;
import com.example.bake_boss_backend.repository.ProductStockrepository;

import jakarta.transaction.Transactional;

@Service
public class ProductStockService {
    @Autowired
    private ProductStockrepository productStockRepository;

    @Autowired
    private ProductRateRepository productRateRepository;

    public List<ProductStock> getProductStockWithInvoiceNotInSalesStock(String customer) {
        return productStockRepository.findProductStockWithInvoiceNotInSalesStock(customer);
    }

    @Transactional
    public ProductRate upsertProductRate(String username, String productName, Integer saleRate) {
        ProductRate existingSetup = productRateRepository.findByUsername(username);
        if (existingSetup != null) {
            existingSetup.setProductName(productName);
            existingSetup.setSaleRate(saleRate);
            return productRateRepository.save(existingSetup);
        } else {
            ProductRate newProductRate = new ProductRate();
            newProductRate.setUsername(username);
            newProductRate.setProductName(productName);
            newProductRate.setSaleRate(saleRate);
            return productRateRepository.save(newProductRate);
        }
    }
}
