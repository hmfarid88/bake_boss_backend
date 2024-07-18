package com.example.bake_boss_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.NetSumAmountDto;
import com.example.bake_boss_backend.dto.PaymentDto;
import com.example.bake_boss_backend.dto.ReceiveDto;
import com.example.bake_boss_backend.entity.MarginSetup;
import com.example.bake_boss_backend.entity.OfficeReceive;
import com.example.bake_boss_backend.entity.PaymentRecord;
import com.example.bake_boss_backend.entity.RetailerPayment;
import com.example.bake_boss_backend.entity.SupplierPayment;
import com.example.bake_boss_backend.repository.OfficeReceiveRepository;
import com.example.bake_boss_backend.repository.PaymentRecordRepository;
import com.example.bake_boss_backend.repository.RetailerPaymentRepository;
import com.example.bake_boss_backend.repository.SupplierPaymentRepository;
import com.example.bake_boss_backend.service.MarginSetupService;
import com.example.bake_boss_backend.service.PaymentRecordService;
import com.example.bake_boss_backend.service.ReceiveService;

@RestController
@RequestMapping("/paymentApi")
public class TransactionController {
    @Autowired
    private MarginSetupService marginSetupService;

    @Autowired
    private PaymentRecordService paymentRecordService;

    @Autowired
    private ReceiveService receiveService;

    private final OfficeReceiveRepository officeReceiveRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final RetailerPaymentRepository retailerPaymentRepository;
    private final SupplierPaymentRepository supplierPaymentRepository;

    TransactionController(OfficeReceiveRepository officeReceiveRepository,
            PaymentRecordRepository paymentRecordRepository,
            RetailerPaymentRepository retailerPaymentRepository,
            SupplierPaymentRepository supplierPaymentRepository) {
        this.officeReceiveRepository = officeReceiveRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.retailerPaymentRepository = retailerPaymentRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
    }

    @PutMapping("/marginSetup")
    public ResponseEntity<MarginSetup> saveOrUpdateMarginSetup(@RequestBody MarginSetup marginSetup) {
        MarginSetup savedMarginSetup = marginSetupService.saveOrUpdateMarginSetup(marginSetup);
        return ResponseEntity.ok(savedMarginSetup);
    }

    @PostMapping("/officeReceive")
    public OfficeReceive newItem(@RequestBody OfficeReceive officeReceive) {
        return officeReceiveRepository.save(officeReceive);
    }

    @PostMapping("/paymentRecord")
    public PaymentRecord newItem(@RequestBody PaymentRecord paymentRecord) {
        return paymentRecordRepository.save(paymentRecord);
    }

    @PostMapping("/retailerPayment")
    public RetailerPayment newItem(@RequestBody RetailerPayment retailerPayment) {
        return retailerPaymentRepository.save(retailerPayment);
    }

    @PostMapping("/supplierPayment")
    public SupplierPayment newItem(@RequestBody SupplierPayment supplierPayment) {
        return supplierPaymentRepository.save(supplierPayment);
    }

    @GetMapping("/getMargin")
    public ResponseEntity<MarginSetup> getMarginSetup(@RequestParam String username) {
        MarginSetup marginSetup = marginSetupService.getMarginSetupByUsername(username);
        if (marginSetup != null) {
            return ResponseEntity.ok(marginSetup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/net-sum-before-today")
    public NetSumAmountDto getNetSumAmountBeforeToday(@RequestParam String username, LocalDate date) {
        return paymentRecordService.getNetSumAmountBeforeToday(username, date);
    }

    @GetMapping("/payments/today")
    public List<PaymentDto> getPaymentsForToday(@RequestParam String username, LocalDate date) {
        return paymentRecordService.getPaymentsForToday(username, date);
    }

    @GetMapping("/receives/today")
    public List<ReceiveDto> getReceivesForToday(@RequestParam String username, LocalDate date) {
        return receiveService.findReceivesForToday(username, date);
    }
}
