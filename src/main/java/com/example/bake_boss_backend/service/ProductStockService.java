package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.PendingStockDto;
import com.example.bake_boss_backend.dto.RequisitionSummaryDTO;
import com.example.bake_boss_backend.entity.MaterialsStock;
import com.example.bake_boss_backend.entity.ProductRate;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.entity.Requisition;
import com.example.bake_boss_backend.entity.SalesStock;
import com.example.bake_boss_backend.repository.MaterialsRepository;
import com.example.bake_boss_backend.repository.ProductRateRepository;
import com.example.bake_boss_backend.repository.ProductStockrepository;
import com.example.bake_boss_backend.repository.RequisitionRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductStockService {
    @Autowired
    private ProductStockrepository productStockRepository;

    @Autowired
    private ProductRateRepository productRateRepository;

    @Autowired
    private MaterialsRepository materialsRepository;

    @Autowired
    private RequisitionRepository requisitionRepository;

    public List<PendingStockDto> getProductStockWithInvoiceNotInSalesStock(String customer) {
        return productStockRepository.findProductStockWithInvoiceNotInSalesStock(customer);
    }

    @Transactional
    public ProductRate upsertProductRate(String username, String productName, Double saleRate) {
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

   
    public List<Requisition> saveAllRequisitions(List<Requisition> requisitions) {
        return requisitionRepository.saveAll(requisitions);
    }

    public List<RequisitionSummaryDTO> getSumOfProductQtyGroupedByUsername() {
        return requisitionRepository.findSumOfProductQtyGroupedByUsername();
    }
    
    @Transactional
    public void acceptRequisition(Long reqId) {
        requisitionRepository.updateStatusByReqId(reqId, "accepted");
    }

    public List<ProductStock> getProductStockByUsernameAndInvoiceNo(String customer, String invoiceNo) {
        return productStockRepository.findByCustomerAndInvoiceNo(customer, invoiceNo);
    }

    public List<Object[]> getTotalMaterialsQtyForUsedStatusInCurrentMonth(String username) {
        LocalDate currentDate = LocalDate.now();
        return materialsRepository.findTotalMaterialsQtyForUsedStatusInCurrentMonth(currentDate, username);
    }

    public List<Object[]> getDatewiseUsedMaterials(String username, LocalDate startDate, LocalDate enDate) {
        return materialsRepository.findDatewiseUsedMaterialsByUsername(username, startDate, enDate);
    }
}

