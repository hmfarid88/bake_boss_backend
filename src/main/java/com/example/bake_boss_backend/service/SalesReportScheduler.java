package com.example.bake_boss_backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesReportScheduler {
    private final SalesReportService salesReportService;

    // Runs every day at 2 PM, 6 PM, and 9 PM Bangladesh time
    @Scheduled(cron = "0 0 14,18,21 * * *", zone = "Asia/Dhaka")
    public void sendScheduledSaleReports() {
        String email = "aurora.salesreport2025@gmail.com"; 
        
        salesReportService.sendTodaySaleReport(email);
        // System.out.println("Daily sale report sent at: " + java.time.LocalTime.now());
    }
}
