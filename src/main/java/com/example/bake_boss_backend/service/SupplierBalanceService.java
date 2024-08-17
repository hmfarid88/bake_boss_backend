package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.DetailsSupplierDTO;
import com.example.bake_boss_backend.dto.DetailsSupplierPayDTO;
import com.example.bake_boss_backend.dto.SupplierBalanceDTO;
import com.example.bake_boss_backend.repository.MaterialsRepository;
import com.example.bake_boss_backend.repository.SupplierPaymentRepository;

@Service
public class SupplierBalanceService {
    @Autowired
    private MaterialsRepository materialsStockRepository;

    @Autowired
    private SupplierPaymentRepository supplierPaymentRepository;

    public List<SupplierBalanceDTO> calculateSuppliersBalanceByUsername(String username) {
        List<Object[]> materialCosts = materialsStockRepository
                .findTotalMaterialCostGroupedBySupplierAndUsername(username);
        List<Object[]> payments = supplierPaymentRepository.findTotalPaymentGroupedBySupplierAndUsername(username);

        Map<String, Double> balanceMap = new HashMap<>();

        // Calculate the total material costs by supplierName
        for (Object[] row : materialCosts) {
            String supplierName = (String) row[0];
            Double totalMaterialCost = (Double) row[1];
            balanceMap.put(supplierName, totalMaterialCost);
        }

        // Subtract the total payments by supplierName
        for (Object[] row : payments) {
            String supplierName = (String) row[0];
            Double totalPayment = (Double) row[1];
            balanceMap.put(supplierName, balanceMap.getOrDefault(supplierName, 0.0) - totalPayment);
        }

        // Convert the map to a list of DTOs
        return balanceMap.entrySet().stream()
                .map(entry -> new SupplierBalanceDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<DetailsSupplierDTO> getSupplierDetails(String username, String supplierName, LocalDate startDate,
            LocalDate endDate) {
        return materialsStockRepository.findMaterialsValueBySupplierAndUsername(username, supplierName, startDate,
                endDate);
    }

    public List<DetailsSupplierPayDTO> getSupplierPayDetails(String username, String supplierName, LocalDate startDate,
            LocalDate endDate) {
        return supplierPaymentRepository.findPaymentValueBySupplierAndUsername(username, supplierName, startDate,
                endDate);
    }
}
