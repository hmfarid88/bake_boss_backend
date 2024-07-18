package com.example.bake_boss_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.OutletInvoiceDTO;
import com.example.bake_boss_backend.entity.CustomerInfo;
import com.example.bake_boss_backend.entity.SalesStock;
import com.example.bake_boss_backend.entity.ShopInfo;
import com.example.bake_boss_backend.repository.CustomerInfoRepository;
import com.example.bake_boss_backend.repository.SalesStockRepository;
import com.example.bake_boss_backend.service.ShopInfoService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    private final SalesStockRepository salesStockRepository;
    private final CustomerInfoRepository customerInfoRepository;
    

    @Autowired
    public InvoiceController(SalesStockRepository salesStockRepository, CustomerInfoRepository customerInfoRepository,
            ShopInfoService shopInfoService) {
        this.salesStockRepository = salesStockRepository;
        this.customerInfoRepository = customerInfoRepository;
        this.shopInfoService = shopInfoService;
    }

    @GetMapping("/outletInvoice")
    public ResponseEntity<OutletInvoiceDTO> getInvoiceData(@RequestParam String soldInvoice) {
        List<SalesStock> salesStockList = salesStockRepository.findBySoldInvoice(soldInvoice);
        Optional<CustomerInfo> customerInfoOpt = customerInfoRepository.findBySoldInvoice(soldInvoice);

        if (!customerInfoOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CustomerInfo customerInfo = customerInfoOpt.get();
        OutletInvoiceDTO response = new OutletInvoiceDTO(salesStockList, customerInfo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private final ShopInfoService shopInfoService;

    @PutMapping("/addShopInfo")
    public ShopInfo saveOrUpdateShopInfo(@RequestBody ShopInfo shopInfo) {
        return shopInfoService.saveOrUpdateShopInfo(shopInfo);
    }

    @GetMapping("/getShopInfo")
    public Optional<ShopInfo> getShopInfo(@RequestParam String username) {
        return shopInfoService.getShopInfoByUsername(username);
    }
}
