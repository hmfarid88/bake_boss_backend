package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.SalesStockDTO;
import com.example.bake_boss_backend.entity.ProductRate;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.entity.SalesStock;
import com.example.bake_boss_backend.repository.ProductRateRepository;
import com.example.bake_boss_backend.repository.ProductStockrepository;
import com.example.bake_boss_backend.repository.SalesStockRepository;

import jakarta.transaction.Transactional;

@Service
public class SalesStockService {
    @Autowired
    private ProductStockrepository productStockRepository;

    @Autowired
    private SalesStockRepository salesStockRepository;

    @Autowired
    private ProductRateRepository productRateRepository;

    public List<ProductStock> getProductStockWithInvoiceNotInSalesStock(String customer) {
        return productStockRepository.findProductStockWithInvoiceNotInSalesStock(customer);
    }

    public List<SalesStockDTO> getAllSalesStockWithRate(String username) {
        List<SalesStock> salesStocks = salesStockRepository.findLastByProductNameAndUsername(username);
        return salesStocks.stream().map(salesStock -> {
            Optional<ProductRate> productRateOpt = productRateRepository
                    .findByProductNameAndUsername(salesStock.getProductName(), salesStock.getUsername());
            int saleRate = productRateOpt.map(ProductRate::getSaleRate).orElse(0); // Default to 0 if not found
            return new SalesStockDTO(
                    salesStock.getProductId(),
                    salesStock.getDate(),
                    salesStock.getCategory(),
                    salesStock.getProductName(),
                    salesStock.getCostPrice(),
                    salesStock.getRemainingQty(),
                    salesStock.getInvoiceNo(),
                    saleRate);
        }).collect(Collectors.toList());
    }

    public List<SalesStockDTO> getSingleSalesStockWithRate(Long productId, String username) {
        List<SalesStock> salesStocks = salesStockRepository.findByProductIdAndUsername(productId, username);
        return salesStocks.stream().map(salesStock -> {
            Optional<ProductRate> productRateOpt = productRateRepository
                    .findByProductNameAndUsername(salesStock.getProductName(), salesStock.getUsername());
            int saleRate = productRateOpt.map(ProductRate::getSaleRate).orElse(0); // Default to 0 if not found
            return new SalesStockDTO(
                    salesStock.getProductId(),
                    salesStock.getDate(),
                    salesStock.getCategory(),
                    salesStock.getProductName(),
                    salesStock.getCostPrice(),
                    salesStock.getRemainingQty(),
                    salesStock.getInvoiceNo(),
                    saleRate);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void insertOrUpdateProductStockInSalesStock(String customer) {
        List<ProductStock> productStocks = getProductStockWithInvoiceNotInSalesStock(customer);

        for (ProductStock productStock : productStocks) {
            Optional<SalesStock> existingSalesStock = salesStockRepository
                    .findLatestSalesStockByProductNameAndUsername(productStock.getProductName(), customer);

            SalesStock newSalesStock = new SalesStock();
            newSalesStock.setDate(LocalDate.now());
            newSalesStock.setCategory(productStock.getCategory());
            newSalesStock.setProductName(productStock.getProductName());

            if (existingSalesStock.isPresent()) {
                SalesStock latestSalesStock = existingSalesStock.get();
                double newCostPrice = (latestSalesStock.getCostPrice() * latestSalesStock.getProductQty()
                        + productStock.getDpRate() * productStock.getProductQty()) /
                        (latestSalesStock.getProductQty() + productStock.getProductQty());
                newSalesStock.setCostPrice(newCostPrice);
                newSalesStock.setRemainingQty(latestSalesStock.getRemainingQty() + productStock.getProductQty());
            } else {
                newSalesStock.setCostPrice(productStock.getDpRate());
                newSalesStock.setRemainingQty(productStock.getProductQty());
            }

            newSalesStock.setProductQty(productStock.getProductQty());
            newSalesStock.setStatus("stored");
            newSalesStock.setUsername(productStock.getCustomer());
            newSalesStock.setInvoiceNo(productStock.getInvoiceNo());

            salesStockRepository.save(newSalesStock);
        }
    }

    public List<SalesStock> getCurrentMonthSoldStocks(String username) {
        return salesStockRepository.findCurrentMonthSoldStocksByUsername(username);
    }

    public List<Object[]> findByUsernameAndDateAndStatus(String username, LocalDate date, String status) {
        return salesStockRepository.findByUsernameAndDateAndStatus(username, date, status);
    }
}
