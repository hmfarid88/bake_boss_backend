package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.PendingVendorDto;
import com.example.bake_boss_backend.dto.SalesStockDTO;
import com.example.bake_boss_backend.entity.ProductRate;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.entity.SalesStock;
import com.example.bake_boss_backend.repository.ProductRateRepository;
import com.example.bake_boss_backend.repository.SalesStockRepository;

import jakarta.transaction.Transactional;

@Service
public class SalesStockService {
    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private SalesStockRepository salesStockRepository;

    @Autowired
    private ProductRateRepository productRateRepository;

    public List<SalesStockDTO> getAllSalesStockWithRate(String username) {
        List<SalesStock> salesStocks = salesStockRepository.findLastByProductNameAndUsername(username);
        return salesStocks.stream().map(salesStock -> {
            Optional<ProductRate> productRateOpt = productRateRepository
                    .findByProductNameAndUsername(salesStock.getProductName(), salesStock.getUsername());
            Double saleRate = productRateOpt.map(ProductRate::getSaleRate).orElse((double) 0);
            Double qty = productRateOpt.map(ProductRate::getQty).orElse((double) 0);
            return new SalesStockDTO(
                    salesStock.getProductId(),
                    salesStock.getDate(),
                    salesStock.getCategory(),
                    salesStock.getProductName(),
                    salesStock.getCostPrice(),
                    salesStock.getRemainingQty(),
                    salesStock.getInvoiceNo(),
                    saleRate,
                    qty);
        }).collect(Collectors.toList());
    }

    public List<SalesStockDTO> getSingleSalesStockWithRate(Long productId, String username) {
        List<SalesStock> salesStocks = salesStockRepository.findByProductIdAndUsername(productId, username);
        return salesStocks.stream().map(salesStock -> {
            Optional<ProductRate> productRateOpt = productRateRepository
                    .findByProductNameAndUsername(salesStock.getProductName(), salesStock.getUsername());
            Double saleRate = productRateOpt.map(ProductRate::getSaleRate).orElse((double) 0);
            Double qty = productRateOpt.map(ProductRate::getQty).orElse((double) 0);
            return new SalesStockDTO(
                    salesStock.getProductId(),
                    salesStock.getDate(),
                    salesStock.getCategory(),
                    salesStock.getProductName(),
                    salesStock.getCostPrice(),
                    salesStock.getRemainingQty(),
                    salesStock.getInvoiceNo(),
                    saleRate,
                    qty);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void insertOrUpdateProductStockInSalesStock(String customer, String invoiceNo) {
        // Fetch ProductStock entries for the specific customer and invoice number
        List<ProductStock> productStocks = productStockService.getProductStockByUsernameAndInvoiceNo(customer, invoiceNo);
    
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
    
    @Transactional
    public void insertOrUpdateSalesStockInSalesStock(String username, String soldInvoice) {
        // Fetch ProductStock entries for the specific customer and invoice number
        List<SalesStock> salesStocks = salesStockRepository.findBySoldInvoice(soldInvoice);
    
        for (SalesStock salesStock : salesStocks) {
            Optional<SalesStock> existingSalesStock = salesStockRepository
                    .findLatestSalesStockByProductNameAndUsername(salesStock.getProductName(), username);
    
            SalesStock newSalesStock = new SalesStock();
            newSalesStock.setDate(LocalDate.now());
            newSalesStock.setCategory(salesStock.getCategory());
            newSalesStock.setProductName(salesStock.getProductName());
    
            if (existingSalesStock.isPresent()) {
                SalesStock latestSalesStock = existingSalesStock.get();
                double newCostPrice = (latestSalesStock.getCostPrice() * latestSalesStock.getProductQty()
                        + salesStock.getCostPrice() * salesStock.getProductQty()) /
                        (latestSalesStock.getProductQty() + salesStock.getProductQty());
                newSalesStock.setCostPrice(newCostPrice);
                newSalesStock.setRemainingQty(latestSalesStock.getRemainingQty() + salesStock.getProductQty());
            } else {
                newSalesStock.setCostPrice(salesStock.getCostPrice());
                newSalesStock.setRemainingQty(salesStock.getProductQty());
            }
    
            newSalesStock.setProductQty(salesStock.getProductQty());
            newSalesStock.setStatus("stored");
            newSalesStock.setUsername(username);
            newSalesStock.setInvoiceNo(soldInvoice);
    
            salesStockRepository.save(newSalesStock);
        }
    }

    public List<SalesStock> getCurrentMonthSoldStocks(String username) {
        return salesStockRepository.findCurrentMonthSoldStocksByUsername(username);
    }

    public List<SalesStock> getCurrentMonthVendorsale(String username) {
        return salesStockRepository.findCurrentMonthVendorSaleByUsername(username);
    }

    public List<SalesStock> getCurrentMonthStockReturned(String username) {
        return salesStockRepository.findCurrentMonthReturnedStocksByUsername(username);
    }

    public List<SalesStock> getCurrentMonthAllStockReturned() {
        return salesStockRepository.findCurrentMonthAllReturnedStocks();
    }

    public List<SalesStock> getDatewiseSoldStocks(String username, LocalDate startDate, LocalDate enDate) {
        return salesStockRepository.findDatewiseSoldStocksByUsername(username, startDate, enDate);
    }

    public List<Object[]> findByUsernameAndDateAndStatus(String username, LocalDate date, String status) {
        return salesStockRepository.findByUsernameAndDateAndStatus(username, date, status);
    }

    public List<SalesStock> getCurrentMonthDataByUsername(String username) {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        
        return salesStockRepository.findCurrentMonthDataByUsername(username, year, month);
    }

    public List<SalesStock> getCurrentMonthEntryByUsername(String username) {
           return salesStockRepository.findCurrentMonthEntryByUsername(username);
    }

    public Double getTotalSaleRateByUsernameAndDate(String username, LocalDate date) {
        return salesStockRepository.findTotalSaleRateByUsernameAndDateBefore(username, date);
    }

    public List<SalesStock> getTodaysSalesByUsername(String username) {
        LocalDate today = LocalDate.now();
        return salesStockRepository.findByUsernameAndDate(username, today);
    }

    public List<PendingVendorDto> getVendorStockByUsernameAndInvoiceNo(String username) {
        return salesStockRepository.findPendingVendorData(username);
    }

    public List<SalesStock> getDetailsvendorSalesStock(String soldInvoice) {
        return salesStockRepository.findBySoldInvoiceNotInStock(soldInvoice);
    }
}
