package com.example.bake_boss_backend.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.PendingVendorDto;
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
            List<SalesStock> savedSalesItems = salesStockRepository.saveAll(saleRequest.getSalesItems());

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
        List<SalesStock> savedSalesStock = salesStockRepository.saveAll(salesStockList);
        return ResponseEntity.ok(savedSalesStock);
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

    @GetMapping("/getOutletAllReturned")
    public List<SalesStock> getCurrentMonthAllStockReturned() {
        return salesStockService.getCurrentMonthAllStockReturned();
    }

    @GetMapping("/getDatewiseOutletSale")
    public List<SalesStock> getDatewiseSale(@RequestParam String username, @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return salesStockService.getDatewiseSoldStocks(username, startDate, endDate);
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

    @GetMapping("/salesStock/current-month")
    public List<SalesStock> getCurrentMonthSales(@RequestParam String username) {
        return salesStockService.getCurrentMonthDataByUsername(username);
    }

    @GetMapping("/salesStock/thismonth-entry")
    public List<SalesStock> getCurrentMonthEntry(@RequestParam String username) {
        return salesStockService.getCurrentMonthEntryByUsername(username);
    }

    @GetMapping("/cashbook/salesTotal")
    public Double getTotalSaleRate(
            @RequestParam String username,
            @RequestParam String date
    ) {
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
}
