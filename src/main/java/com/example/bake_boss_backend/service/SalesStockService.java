package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.LossProfitAnalysis;
import com.example.bake_boss_backend.dto.PendingVendorDto;
import com.example.bake_boss_backend.dto.SaleReportDTO;
import com.example.bake_boss_backend.dto.SalesProfitDto;
import com.example.bake_boss_backend.dto.SalesStockDTO;
import com.example.bake_boss_backend.dto.SixMonthSaleDTO;
import com.example.bake_boss_backend.dto.StockLedgerDTO;
import com.example.bake_boss_backend.dto.SupplierSalesStockDTO;
import com.example.bake_boss_backend.dto.TopSalesDTO;
import com.example.bake_boss_backend.dto.VendorSaleReportDTO;
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
            Double unitRate = productRateOpt.map(ProductRate::getUnitRate).orElse((double) 0);
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
                    unitRate,
                    qty);
        }).collect(Collectors.toList());
    }

    public List<SalesStockDTO> getSingleSalesStockWithRate(Long productId, String username) {
        List<SalesStock> salesStocks = salesStockRepository.findByProductIdAndUsername(productId, username);
        return salesStocks.stream().map(salesStock -> {
            Optional<ProductRate> productRateOpt = productRateRepository
                    .findByProductNameAndUsername(salesStock.getProductName(), salesStock.getUsername());
            Double saleRate = productRateOpt.map(ProductRate::getSaleRate).orElse((double) 0);
            Double unitRate = productRateOpt.map(ProductRate::getUnitRate).orElse((double) 0);
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
                    unitRate,
                    qty);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void insertOrUpdateProductStockInSalesStock(String customer, String invoiceNo) {
        // Fetch ProductStock entries for the specific customer and invoice number
        List<ProductStock> productStocks = productStockService.getProductStockByUsernameAndInvoiceNo(customer,
                invoiceNo);

        for (ProductStock productStock : productStocks) {
            Optional<SalesStock> existingSalesStock = salesStockRepository
                    .findLatestSalesStockByProductNameAndUsername(productStock.getProductName(), customer);

            SalesStock newSalesStock = new SalesStock();
            newSalesStock.setDate(LocalDate.now());
            ZonedDateTime dhakaTime = ZonedDateTime.now(ZoneId.of("Asia/Dhaka"));
            newSalesStock.setTime(dhakaTime.toLocalTime());
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
            ZonedDateTime dhakaTime = ZonedDateTime.now(ZoneId.of("Asia/Dhaka"));
            newSalesStock.setTime(dhakaTime.toLocalTime());
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

    // public List<SaleReportDTO> getCurrentMonthSoldStocks(String username) {
    // return salesStockRepository.findCurrentMonthSoldStocksByUsername(username);
    // }

    public List<SaleReportDTO> getCurrentMonthSoldStocks(String username, int percent) {
        List<SaleReportDTO> fullList = salesStockRepository.findCurrentMonthSoldStocksByUsername(username);
        Map<String, List<SaleReportDTO>> grouped = fullList.stream()
                .collect(Collectors.groupingBy(s -> String.valueOf(s.getDate())));
        // .collect(Collectors.groupingBy(s -> s.getDate() + "_" + s.getProductName()));

        List<SaleReportDTO> finalList = new ArrayList<>();

        // grouped.forEach((key, list) -> {
        // int limit = (int) Math.ceil((percent / 100.0) * list.size());
        // finalList.addAll(list.stream().limit(limit).toList());
        // });
        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // ascending date
                .forEach(entry -> {
                    List<SaleReportDTO> list = entry.getValue();
                    int limit = (int) Math.ceil((percent / 100.0) * list.size());
                    finalList.addAll(list.stream().limit(limit).toList());
                });

        return finalList;
    }

    // public List<VendorSaleReportDTO> getCurrentMonthVendorsale(String username) {
    // return salesStockRepository.findCurrentMonthVendorSaleByUsername(username);
    // }

    public List<VendorSaleReportDTO> getCurrentMonthVendorsale(String username, int percent) {
        List<VendorSaleReportDTO> fullList = salesStockRepository.findCurrentMonthVendorSaleByUsername(username);

        Map<String, List<VendorSaleReportDTO>> grouped = fullList.stream()
                // .collect(Collectors.groupingBy(s -> s.getDate() + "_" + s.getProductName()));
                .collect(Collectors.groupingBy(s -> String.valueOf(s.getDate())));

        List<VendorSaleReportDTO> finalList = new ArrayList<>();

        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // ascending date
                .forEach(entry -> {
                    List<VendorSaleReportDTO> list = entry.getValue();
                    int limit = (int) Math.ceil((percent / 100.0) * list.size());
                    finalList.addAll(list.stream().limit(limit).toList());
                });

        return finalList;
    }

    public List<SalesStock> getCurrentMonthStockReturned(String username) {
        return salesStockRepository.findCurrentMonthReturnedStocksByUsername(username);
    }

    public List<SalesStock> getDatewiseStockReturned(String username, LocalDate startDate, LocalDate endDate) {
        return salesStockRepository.findDatewiseReturnedStocksByUsername(username, startDate, endDate);
    }

    public List<SalesStock> getCurrentMonthAllStockReturned() {
        return salesStockRepository.findCurrentMonthAllReturnedStocks();
    }

    public List<SalesStock> getDatewiseStockReturned(LocalDate startDate, LocalDate endDate) {
        return salesStockRepository.findDatewiseReturnedStocks(startDate, endDate);
    }

    public List<SalesStock> getReturnedByInvoiceno(String username, String invoiceNo) {
        return salesStockRepository.findReturnedStocksByInvoice(username, invoiceNo);
    }

    // public List<SaleReportDTO> getDatewiseSoldStocks(String username, LocalDate
    // startDate, LocalDate endDate) {
    // return salesStockRepository.findDatewiseSoldStocksByUsername(username,
    // startDate, endDate);
    // }

    public List<SaleReportDTO> getDatewiseSoldStocks(String username, LocalDate startDate, LocalDate endDate,
            int percent) {
        List<SaleReportDTO> fullList = salesStockRepository.findDatewiseSoldStocksByUsername(username, startDate,
                endDate);
        Map<String, List<SaleReportDTO>> grouped = fullList.stream()
                .collect(Collectors.groupingBy(s -> String.valueOf(s.getDate())));
        List<SaleReportDTO> finalList = new ArrayList<>();
        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    List<SaleReportDTO> list = entry.getValue();
                    int limit = (int) Math.ceil((percent / 100.0) * list.size());
                    finalList.addAll(list.stream().limit(limit).toList());
                });

        return finalList;
    }

    public List<StockLedgerDTO> getDatewiseStockLedger(String username, LocalDate startDate, LocalDate endDate) {
        return salesStockRepository.findDatewiseStockLedgerUsername(username, startDate, endDate);
    }

    // public List<VendorSaleReportDTO> getDatewiseVendorSale(String username,
    // LocalDate startDate, LocalDate endDate) {
    // return salesStockRepository.findDatewiseVendorSaleByUsername(username,
    // startDate, enDate);
    // }

    public List<VendorSaleReportDTO> getDatewiseVendorSale(String username, LocalDate startDate, LocalDate endDate,
            int percent) {
        List<VendorSaleReportDTO> fullList = salesStockRepository.findDatewiseVendorSaleByUsername(username, startDate,
                endDate);
        Map<String, List<VendorSaleReportDTO>> grouped = fullList.stream()
                // .collect(Collectors.groupingBy(s -> s.getDate() + "_" + s.getProductName()));
                .collect(Collectors.groupingBy(s -> String.valueOf(s.getDate())));
        List<VendorSaleReportDTO> finalList = new ArrayList<>();
        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    List<VendorSaleReportDTO> list = entry.getValue();
                    int limit = (int) Math.ceil((percent / 100.0) * list.size());
                    finalList.addAll(list.stream().limit(limit).toList());
                });

        return finalList;
    }

    // public List<Object[]> findByUsernameAndDateAndStatus(String username,
    // LocalDate date, String status) {
    // return salesStockRepository.findByUsernameAndDateAndStatus(username, date,
    // status);
    // }
    public List<Object[]> findByUsernameAndDateAndStatus(String username, LocalDate date, String status, int percent) {
        List<Object[]> fullList = salesStockRepository.findByUsernameAndDateAndStatus(username, date, status);
        Map<LocalDate, List<Object[]>> grouped = fullList.stream()
                .collect(Collectors.groupingBy(s -> (LocalDate) s[0]));
        List<Object[]> finalList = new ArrayList<>();

        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    List<Object[]> list = entry.getValue();
                    list.sort(Comparator.comparing(o -> (LocalDate) o[0]));
                    int limit = (int) Math.ceil((percent / 100.0) * list.size());
                    finalList.addAll(list.stream().limit(limit).toList());
                });

        return finalList;
    }

    public List<StockLedgerDTO> getCurrentMonthDataByUsername(String username) {
        return salesStockRepository.findCurrentMonthDataByUsername(username);
    }

    public List<SupplierSalesStockDTO> getCurrentMonthEntryByUsername(String username) {
        return salesStockRepository.findCurrentMonthEntryByUsername(username);
    }

    public List<SupplierSalesStockDTO> getDatewiseEntryByUsername(LocalDate startDate, LocalDate endDate,
            String username) {
        return salesStockRepository.findDatewiseEntryByUsername(startDate, endDate, username);
    }

    // public List<SalesProfitDto> getCurrentMonthProfitByUsername(String username)
    // {
    // return salesStockRepository.findMonthlyProfit(username);
    // }

    public List<SalesProfitDto> getCurrentMonthProfitByUsername(String username, int percent) {
        List<SalesProfitDto> fullList = salesStockRepository.findMonthlyProfit(username);
        Map<String, List<SalesProfitDto>> grouped = fullList.stream()
                // .collect(Collectors.groupingBy(SalesProfitDto::getProductName));
                .collect(Collectors.groupingBy(s -> String.valueOf(s.getDate())));

        List<SalesProfitDto> finalList = new ArrayList<>();

        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // ascending date
                .forEach(entry -> {
                    List<SalesProfitDto> list = entry.getValue();
                    int limit = (int) Math.ceil((percent / 100.0) * list.size());
                    finalList.addAll(list.stream().limit(limit).toList());
                });

        return finalList;
    }

    // public List<SalesProfitDto> getDatewiseProfitByUsername(String username,
    // LocalDate startDate, LocalDate endDate) {
    // return salesStockRepository.findDatewiseProfit(username, startDate, endDate);
    // }

    public List<SalesProfitDto> getDatewiseProfitByUsername(String username, LocalDate startDate, LocalDate endDate,
            int percent) {
        List<SalesProfitDto> fullList = salesStockRepository.findDatewiseProfit(username, startDate, endDate);
        Map<String, List<SalesProfitDto>> grouped = fullList.stream()
                // .collect(Collectors.groupingBy(SalesProfitDto::getProductName));
                .collect(Collectors.groupingBy(s -> String.valueOf(s.getDate())));

        List<SalesProfitDto> finalList = new ArrayList<>();

        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // ascending date
                .forEach(entry -> {
                    List<SalesProfitDto> list = entry.getValue();
                    int limit = (int) Math.ceil((percent / 100.0) * list.size());
                    finalList.addAll(list.stream().limit(limit).toList());
                });

        return finalList;
    }

    public Double getTotalSaleRateByUsernameAndDate(String username, LocalDate date) {
        return salesStockRepository.findTotalSaleRateByUsernameAndDateBefore(username, date);
    }

    // public List<SaleReportDTO> getTodaysSalesByUsername(String username) {
    // return salesStockRepository.findTodaysSaleByUsername(username);
    // }

    public List<SaleReportDTO> getTodaysSalesByUsername(String username, int percent) {

        List<SaleReportDTO> fullList = salesStockRepository.findTodaysSaleByUsername(username);

        // Group by productName (you can also add date or time if needed)
        Map<String, List<SaleReportDTO>> grouped = fullList.stream()
                // .collect(Collectors.groupingBy(SaleReportDTO::getProductName));
                .collect(Collectors.groupingBy(s -> String.valueOf(s.getDate())));

        List<SaleReportDTO> finalList = new ArrayList<>();

        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // ascending date
                .forEach(entry -> {
                    List<SaleReportDTO> list = entry.getValue();
                    int limit = (int) Math.ceil((percent / 100.0) * list.size());
                    finalList.addAll(list.stream().limit(limit).toList());
                });

        return finalList;
    }

    public List<PendingVendorDto> getVendorStockByUsernameAndInvoiceNo(String username) {
        return salesStockRepository.findPendingVendorData(username);
    }

    public List<SalesStock> getDetailsvendorSalesStock(String soldInvoice) {
        return salesStockRepository.findBySoldInvoiceNotInStock(soldInvoice);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateProductQty(Long productId, String username, Double newQty) {
        Optional<SalesStock> optionalStock = salesStockRepository.findById(productId);

        Map<String, String> response = new HashMap<>();

        if (optionalStock.isPresent()) {
            SalesStock existingStock = optionalStock.get();
            Double oldQty = existingStock.getProductQty();
            Double qtyDifference = newQty - oldQty;

            // Fetch the remainingQty from the database
            Double remainingQty = salesStockRepository.getRemainingQty(existingStock.getProductName(), username);

            // Check if newQty exceeds available remainingQty
            if (qtyDifference > 0 && remainingQty < qtyDifference) {
                response.put("status", "error");
                response.put("message", "Insufficient remaining quantity. Update failed.");
                return ResponseEntity.badRequest().body(response);
            }

            existingStock.setProductQty(newQty);

            if (qtyDifference > 0) {
                salesStockRepository.reduceRemainingQty(existingStock.getProductName(), username, productId,
                        qtyDifference);
            } else if (qtyDifference < 0) {
                salesStockRepository.increaseRemainingQty(existingStock.getProductName(), username, productId,
                        Math.abs(qtyDifference));
            }

            salesStockRepository.save(existingStock);

            response.put("status", "success");
            response.put("message", "Product quantity updated successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "SalesStock not found for productId: " + productId);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateSalesProductQty(Long productId, String username, Double newQty) {
        Map<String, String> response = new HashMap<>();

        List<SalesStock> list = salesStockRepository.findByProductIdAndUsername(productId, username);

        if (list.isEmpty()) {
            response.put("message", "Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // take single or last row
        SalesStock salesStock = list.get(0); // or list.get(list.size() - 1);

        // overwrite remaining qty
        salesStock.setRemainingQty(newQty);

        salesStockRepository.save(salesStock);

        response.put("message", "Remaining quantity overwritten successfully");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public void updateDiscount(Long productId, Double newDiscount) {
        Optional<SalesStock> existingStockOptional = salesStockRepository.findById(productId);
        if (!existingStockOptional.isPresent()) {
            return;
        }

        SalesStock existingStock = existingStockOptional.get();
        existingStock.setDiscount(newDiscount);
        salesStockRepository.save(existingStock);
    }

    @Transactional
    public void deleteProductById(Long productId, String username) {
        Optional<SalesStock> existingStockOptional = salesStockRepository.findById(productId);
        if (!existingStockOptional.isPresent()) {
            return;
        }
        SalesStock existingStock = existingStockOptional.get();
        Double productQty = existingStock.getProductQty();
        String productName = existingStock.getProductName();

        salesStockRepository.deleteById(productId);
        List<SalesStock> affectedStocks = salesStockRepository
                .findByUsernameAndProductNameAndProductIdGreaterThan(username, productName, productId);

        for (SalesStock stock : affectedStocks) {
            stock.setRemainingQty(stock.getRemainingQty() + productQty);
            salesStockRepository.save(stock);
        }
    }

    // public List<TopSalesDTO> getTop10SoldProducts(String username) {
    // List<TopSalesDTO> soldProducts =
    // salesStockRepository.findTop10SoldProductsByUsernameAndStatusSold(username);
    // return soldProducts.stream().limit(10).toList();
    // }

    public List<TopSalesDTO> getTop10SoldProducts(String username, int percent) {

        List<TopSalesDTO> fullList = salesStockRepository.findTop10SoldProductsByUsernameAndStatusSold(username);

        // Group by productName
        Map<String, List<TopSalesDTO>> grouped = fullList.stream()
                .collect(Collectors.groupingBy(TopSalesDTO::getProductName));

        List<TopSalesDTO> finalList = new ArrayList<>();

        grouped.forEach((product, list) -> {
            int limit = (int) Math.ceil((percent / 100.0) * list.size());
            finalList.addAll(list.stream().limit(limit).toList());
        });

        return finalList;
    }

    public List<TopSalesDTO> getTop10SoldProductsToday(String username) {
        List<TopSalesDTO> soldProducts = salesStockRepository
                .findTop10SoldProductsByUsernameAndStatusSoldForToday(username);
        return soldProducts.stream().limit(10).toList();
    }

    public List<SixMonthSaleDTO> getLastSixMonthsSalesByCategory(String username) {
        LocalDate sixMonthsAgo = LocalDate.now().minus(6, ChronoUnit.MONTHS);
        return salesStockRepository.findLastSixMonthsSalesByCategory(username, sixMonthsAgo);
    }

    public List<LossProfitAnalysis> getLastTwelveMonthsProfitLoss(String username) {
        LocalDate twelveMonthsAgo = LocalDate.now().minus(12, ChronoUnit.MONTHS);
        return salesStockRepository.findLastTwelveMonthsProfitLoss(username, twelveMonthsAgo);
    }

}
