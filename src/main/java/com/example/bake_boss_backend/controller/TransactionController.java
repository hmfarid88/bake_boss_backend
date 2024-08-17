package com.example.bake_boss_backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import com.example.bake_boss_backend.entity.Expense;
import com.example.bake_boss_backend.entity.MarginSetup;
import com.example.bake_boss_backend.entity.OfficeReceive;
import com.example.bake_boss_backend.entity.OfficePayment;
import com.example.bake_boss_backend.entity.RetailerPayment;
import com.example.bake_boss_backend.entity.SupplierPayment;
import com.example.bake_boss_backend.repository.ExpenseRepository;
import com.example.bake_boss_backend.repository.OfficeReceiveRepository;
import com.example.bake_boss_backend.repository.OfficePaymentRepository;
import com.example.bake_boss_backend.repository.RetailerPaymentRepository;
import com.example.bake_boss_backend.repository.SupplierPaymentRepository;
import com.example.bake_boss_backend.service.ExpenseService;
import com.example.bake_boss_backend.service.MarginSetupService;
import com.example.bake_boss_backend.service.OfficePaymentService;
import com.example.bake_boss_backend.service.ReceiveService;
import com.example.bake_boss_backend.service.RetailerPaymentService;
import com.example.bake_boss_backend.service.SupplierPaymentService;

@RestController
@RequestMapping("/paymentApi")
public class TransactionController {
    @Autowired
    private MarginSetupService marginSetupService;

    @Autowired
    private OfficePaymentService officePaymentService;

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private RetailerPaymentService retailerPaymentService;

    private final OfficeReceiveRepository officeReceiveRepository;
    private final OfficePaymentRepository officePaymentRepository;
    private final RetailerPaymentRepository retailerPaymentRepository;
    private final SupplierPaymentRepository supplierPaymentRepository;
    private final ExpenseRepository expenseRepository;

    TransactionController(OfficeReceiveRepository officeReceiveRepository,
            OfficePaymentRepository paymentRecordRepository,
            RetailerPaymentRepository retailerPaymentRepository,
            SupplierPaymentRepository supplierPaymentRepository,
            ExpenseRepository expenseRepository) {
        this.officeReceiveRepository = officeReceiveRepository;
        this.officePaymentRepository = paymentRecordRepository;
        this.retailerPaymentRepository = retailerPaymentRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
        this.expenseRepository = expenseRepository;
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

    @PostMapping("/officePayment")
    public OfficePayment newItem(@RequestBody OfficePayment paymentRecord) {
        return officePaymentRepository.save(paymentRecord);
    }

    @PostMapping("/expenseRecord")
    public Expense expenseItem(@RequestBody Expense expenseRecord) {
        return expenseRepository.save(expenseRecord);
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
    public Optional<MarginSetup> getMarginSetup(@RequestParam String username, @RequestParam String productName) {
        return marginSetupService.getMarginByUserAndProduct(username, productName);
    }

    @GetMapping("/net-sum-before-today")
    public NetSumAmountDto getNetSumAmountBeforeToday(@RequestParam String username, LocalDate date) {
        return officePaymentService.getNetSumAmountBeforeToday(username, date);
    }

    @GetMapping("/payments/today")
    public List<PaymentDto> getPaymentsForToday(@RequestParam String username, LocalDate date) {
        return officePaymentService.getPaymentsForToday(username, date);
    }

    @GetMapping("/receives/today")
    public List<ReceiveDto> getReceivesForToday(@RequestParam String username, LocalDate date) {
        return receiveService.findReceivesForToday(username, date);
    }

    @GetMapping("/getExpense")
    public List<Expense> getCurrentMonthExpenses(@RequestParam String username) {
        return expenseService.getCurrentMonthExpenses(username);
    }

    @GetMapping("/getOfficePay")
    public List<OfficePayment> getPaymentsForCurrentMonth(@RequestParam String username) {
        return officePaymentService.getPaymentsForCurrentMonth(username);
    }

    @GetMapping("/getSupplierPay")
    public List<SupplierPayment> getsupplierForCurrentMonth(@RequestParam String username) {
        return supplierPaymentService.getSupplierForCurrentMonth(username);
    }

    @GetMapping("/getOfficeReceive")
    public List<OfficeReceive> getReceiveForCurrentMonth(@RequestParam String username) {
        return receiveService.getReceivesForCurrentMonth(username);
    }

    @GetMapping("/getRetailerPayment")
    public List<RetailerPayment> getRetailerPayForCurrentMonth(@RequestParam String username) {
        return retailerPaymentService.getRetailerPayForCurrentMonth(username);
    }
}
