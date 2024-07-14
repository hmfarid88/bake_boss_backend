package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.ReceiveDto;
import com.example.bake_boss_backend.repository.OfficeReceiveRepository;
import com.example.bake_boss_backend.repository.RetailerPaymentRepository;

@Service
public class ReceiveService {
    @Autowired
    private OfficeReceiveRepository officeReceiveRepository;

    @Autowired
    private RetailerPaymentRepository retailerPaymentRepository;

    public List<ReceiveDto> findReceivesForToday(String username, LocalDate date) {
        List<ReceiveDto> receives = new ArrayList<>();
        receives.addAll(officeReceiveRepository.findOfficeReceivesForToday(username, date));
        receives.addAll(retailerPaymentRepository.findRetailerPaymentsForToday(username, date));
        return receives;
    }
}
