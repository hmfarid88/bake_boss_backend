package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.RetailerPayment;

import com.example.bake_boss_backend.repository.RetailerPaymentRepository;

@Service
public class RetailerPaymentService {
    @Autowired
    private RetailerPaymentRepository retailerPaymentRepository;

    public List<RetailerPayment> getRetailerPayForCurrentMonth(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return retailerPaymentRepository.findRetailerPayByMonth(year, month, username);
    }
}
