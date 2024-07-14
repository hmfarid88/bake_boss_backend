package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.NetSumAmountDto;
import com.example.bake_boss_backend.dto.PaymentDto;
import com.example.bake_boss_backend.repository.PaymentRecordRepository;
import com.example.bake_boss_backend.repository.RetailerPaymentRepository;
import com.example.bake_boss_backend.repository.SupplierPaymentRepository;

@Service
public class PaymentRecordService {
    @Autowired
    private RetailerPaymentRepository retailerPaymentRepository;

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Autowired
    private SupplierPaymentRepository supplierPaymentRepository;

    public NetSumAmountDto getNetSumAmountBeforeToday(String username, LocalDate date) {
        Double netSumAmount = retailerPaymentRepository.findNetSumAmountBeforeToday(username, date);
        return new NetSumAmountDto(netSumAmount);
    }

    public List<PaymentDto> getPaymentsForToday(String username, LocalDate date) {
        List<PaymentDto> userPayments = paymentRecordRepository.findPaymentsForToday(username, date);
        List<PaymentDto> supplierPayments = supplierPaymentRepository.findSupplierPaymentsForToday(username, date);
        List<PaymentDto> combinedPayments = new ArrayList<>();
        combinedPayments.addAll(userPayments);
        combinedPayments.addAll(supplierPayments);
        return combinedPayments;
    }
}
