package com.example.bake_boss_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.DetailsSupplierDTO;
import com.example.bake_boss_backend.dto.DetailsSupplierPayDTO;
import com.example.bake_boss_backend.dto.SupplierBalanceDTO;
import com.example.bake_boss_backend.service.SupplierBalanceService;

@RestController
@RequestMapping("/supplierBalance")
public class SupplierBalanceController {
     @Autowired
    private SupplierBalanceService supplierBalanceService;

    @GetMapping("/supplier/balance")
    public List<SupplierBalanceDTO> getSuppliersBalanceByUsername(@RequestParam String username) {
        return supplierBalanceService.calculateSuppliersBalanceByUsername(username);
    }

    @GetMapping("/supplier/details")
    public List<DetailsSupplierDTO> getSupplierDetails( @RequestParam String username, @RequestParam String supplierName, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return supplierBalanceService.getSupplierDetails( username, supplierName, startDate, endDate);
    }

    @GetMapping("/supplier/payDetails")
    public List<DetailsSupplierPayDTO> getSupplierPayDetails( @RequestParam String username, @RequestParam String supplierName,@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return supplierBalanceService.getSupplierPayDetails( username, supplierName, startDate, endDate);
    }
}
