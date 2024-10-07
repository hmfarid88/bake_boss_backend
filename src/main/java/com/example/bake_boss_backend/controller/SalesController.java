package com.example.bake_boss_backend.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.TopSalesDTO;
import com.example.bake_boss_backend.dto.PendingVendorDto;
import com.example.bake_boss_backend.dto.SalesProfitDto;
import com.example.bake_boss_backend.dto.SalesRequest;
import com.example.bake_boss_backend.dto.SalesStockDTO;
import com.example.bake_boss_backend.entity.CustomerInfo;
import com.example.bake_boss_backend.entity.SalesStock;
import com.example.bake_boss_backend.repository.CustomerInfoRepository;
import com.example.bake_boss_backend.repository.SalesStockRepository;
import com.example.bake_boss_backend.service.SalesStockService;

@RestController
@RequestMapping("/sales")
public class SalesController {
    @Autowired
    private SalesStockService salesStockService;

    @Autowired
    private SalesStockRepository salesStockRepository;

    @Autowired
    private CustomerInfoRepository customerInfoRepository;

    @GetMapping("/getSalesStock")
    public List<SalesStockDTO> getAllSalesStockWithRate(String username) {
        return salesStockService.getAllSalesStockWithRate(username);
    }

    @GetMapping("/getSingleProduct")
    public List<SalesStockDTO> getSingleProduct(Long productId, String username) {
        return salesStockService.getSingleSalesStockWithRate(productId, username);
    }

    @PostMapping("/addSalesStock")
    public ResponseEntity<String> insertOrUpdateSalesStockInSalesStock(
            @RequestBody Map<String, String> customerData) {
        String username = customerData.get("username");
        String soldInvoice = customerData.get("soldInvoice");

        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }

        if (soldInvoice == null || soldInvoice.isEmpty()) {
            return ResponseEntity.badRequest().body("Invoice number is required");
        }

        salesStockService.insertOrUpdateSalesStockInSalesStock(username, soldInvoice);
        return ResponseEntity.ok("Products added successfully");
    }

   
    @PostMapping("/outletSale")
    public ResponseEntity<?> handleSale(@RequestBody SalesRequest saleRequest) {
        try {
            CustomerInfo savedCustomer = customerInfoRepository.save(saleRequest.getCustomer());
            List<SalesStock> savedSalesItems = new ArrayList<>();

            for (SalesStock salesItem : saleRequest.getSalesItems()) {
                // Find the last SalesStock by productName and username
                Optional<SalesStock> lastSalesStock = salesStockRepository
                        .findTopByProductNameAndUsernameOrderByProductIdDesc(salesItem.getProductName(), salesItem.getUsername());

                if (lastSalesStock.isPresent()) {
                    // Update remainingQty by subtracting the new productQty
                    SalesStock lastStock = lastSalesStock.get();
                    salesItem.setRemainingQty(lastStock.getRemainingQty() - salesItem.getProductQty());
                    salesItem.setTime(LocalTime.now());
                    salesStockRepository.save(salesItem);
                }

                // Save the new sales item
                savedSalesItems.add(salesStockRepository.save(salesItem));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("customer", savedCustomer);
            response.put("salesItems", savedSalesItems);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "An error occurred while processing the sale"));
        }
    }

       @PostMapping("/outletStockReturn")
    public ResponseEntity<List<SalesStock>> addMultipleSalesStock(@RequestBody List<SalesStock> salesStockList) {
        List<SalesStock> savedSalesStockList = new ArrayList<>();
    
        for (SalesStock salesItem : salesStockList) {
            // Find the last SalesStock by productName and username
            Optional<SalesStock> lastSalesStock = salesStockRepository
                    .findTopByProductNameAndUsernameOrderByProductIdDesc(salesItem.getProductName(), salesItem.getUsername());
    
            if (lastSalesStock.isPresent()) {
                // Update remainingQty by subtracting the new productQty
                SalesStock lastStock = lastSalesStock.get();
                salesItem.setRemainingQty(lastStock.getRemainingQty() - salesItem.getProductQty());
                salesItem.setTime(LocalTime.now());
            } else {
                // If no previous stock exists, set remainingQty to a default value
                salesItem.setRemainingQty(salesItem.getProductQty());
                salesItem.setTime(LocalTime.now());
            }
    
            // Save the new sales item and add to savedSalesStockList
            savedSalesStockList.add(salesStockRepository.save(salesItem));
        }
    
        return ResponseEntity.ok(savedSalesStockList);
    }
    

    @GetMapping("/getOutletSale")
    public List<SalesStock> getCurrentMonthSoldStocks(@RequestParam String username) {
        return salesStockService.getCurrentMonthSoldStocks(username);
    }

    @GetMapping("/getVendorSale")
    public List<SalesStock> getCurrentMonthVendorSale(@RequestParam String username) {
        return salesStockService.getCurrentMonthVendorsale(username);
    }

    @GetMapping("/getOutletReturned")
    public List<SalesStock> getCurrentMonthStockReturned(@RequestParam String username) {
        return salesStockService.getCurrentMonthStockReturned(username);
    }

    @GetMapping("/getDatewiseOutletReturned")
    public List<SalesStock> getDatewiseStockReturned(@RequestParam String username, LocalDate startDate,
            LocalDate endDate) {
        return salesStockService.getDatewiseStockReturned(username, startDate, endDate);
    }

    @GetMapping("/getMonthlySalesProfit")
    public List<SalesProfitDto> getCurrentMonthSalesProfit(@RequestParam String username) {
        return salesStockService.getCurrentMonthProfitByUsername(username);
    }

    @GetMapping("/getDatewiseSalesProfit")
    public List<SalesProfitDto> getDatewiseSalesProfit(@RequestParam String username, LocalDate startDate,
            LocalDate endDate) {
        return salesStockService.getDatewiseProfitByUsername(username, startDate, endDate);
    }

    @GetMapping("/getOutletAllReturned")
    public List<SalesStock> getCurrentMonthAllStockReturned() {
        return salesStockService.getCurrentMonthAllStockReturned();
    }

    @GetMapping("/getDatewiseOutletSale")
    public List<SalesStock> getDatewiseSale(@RequestParam String username, @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return salesStockService.getDatewiseSoldStocks(username, startDate, endDate);
    }

    @GetMapping("/getDatewiseVendorSale")
    public List<SalesStock> getDatewiseVendorSale(@RequestParam String username, @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return salesStockService.getDatewiseVendorSale(username, startDate, endDate);
    }

    @GetMapping("/cashbook/dateWiseSale")
    public ResponseEntity<List<Object[]>> findByUsernameAndDateAndStatus(
            @RequestParam String username,
            @RequestParam String date,
            @RequestParam String status) {
        LocalDate localDate = LocalDate.parse(date);
        List<Object[]> salesStocks = salesStockService.findByUsernameAndDateAndStatus(username, localDate, status);
        return ResponseEntity.ok(salesStocks);
    }

    @GetMapping("/monthlyStockLedger")
    public List<SalesStock> getCurrentMonthSales(@RequestParam String username) {
        return salesStockService.getCurrentMonthDataByUsername(username);
    }

    @GetMapping("/datewiseStockLedger")
    public List<SalesStock> getDatewiseStockLedger(@RequestParam String username, LocalDate startDate,
            LocalDate endDate) {
        return salesStockService.getDatewiseStockLedger(username, startDate, endDate);
    }

    @GetMapping("/salesStock/thismonth-entry")
    public List<SalesStock> getCurrentMonthEntry(@RequestParam String username) {
        return salesStockService.getCurrentMonthEntryByUsername(username);
    }

    @GetMapping("/datewiseEntryLedger")
    public List<SalesStock> getDatewiseEntry(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return salesStockService.getDatewiseEntryByUsername(username, startDate, endDate);
    }

    @GetMapping("/cashbook/previousSalesTotal")
    public Double getTotalSaleRate(
            @RequestParam String username,
            @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return salesStockService.getTotalSaleRateByUsernameAndDate(username, parsedDate);
    }

    @GetMapping("/sales/today")
    public List<SalesStock> getTodaysSales(@RequestParam String username) {
        return salesStockService.getTodaysSalesByUsername(username);
    }

    @GetMapping("/pendingVendorStock")
    public List<PendingVendorDto> getProductStockByUsernameAndInvoiceNo(@RequestParam String username) {
        return salesStockService.getVendorStockByUsernameAndInvoiceNo(username);
    }

    @GetMapping("/pendingDetailsVendor")
    public List<SalesStock> getDetailsVendorStock(@RequestParam String soldInvoice) {
        return salesStockService.getDetailsvendorSalesStock(soldInvoice);
    }

    @PutMapping("/update-quantity/{productId}")
    public ResponseEntity<String> updateProductQty(
            @PathVariable Long productId,
            @RequestParam Double newQty) {
        salesStockService.updateProductQty(productId, newQty);
        return ResponseEntity.ok("Product quantity updated successfully");
    }

    @PutMapping("/update-discount/{productId}")
    public ResponseEntity<Void> updateDiscount(
            @PathVariable Long productId,
            @RequestParam Double newDiscount) {
        salesStockService.updateDiscount(productId, newDiscount);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        salesStockService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current-month/saleprogress")
    public ResponseEntity<List<TopSalesDTO>> getCurrentMonthSalesData(@RequestParam String username) {
        List<TopSalesDTO> salesData = salesStockService.getTop10SoldProducts(username);
        return ResponseEntity.ok(salesData);
    }

    @GetMapping("/lastsixmonth/saleprogress")
public ResponseEntity<List<Map<String, Object>>> getSixMonthSalesData(@RequestParam String username) {
    List<Object[]> salesData = salesStockService.getLastSixMonthsSalesByCategory(username);

      List<Map<String, Object>> formattedSalesData = new ArrayList<>();
    for (Object[] row : salesData) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("month", row[0]);  // Month number
        dataMap.put("category", row[1]);  // Category name
        dataMap.put("totalSale", row[2]);  // Total sale value
        formattedSalesData.add(dataMap);
    }
    return ResponseEntity.ok(formattedSalesData);
}

@GetMapping("/lasttwelvemonth/profitloss")
public ResponseEntity<List<Object[]>> getLastTwelveMonthsProfitLoss(@RequestParam String username) {
    List<Object[]> profitLossData = salesStockService.getLastTwelveMonthsProfitLoss(username);
    return ResponseEntity.ok(profitLossData);
}


}
