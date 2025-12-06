package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.repository.SalesStockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesReportService {
    private final SalesStockRepository salesStockRepository;
    private final EmailService emailService;

    public void sendTodaySaleReport(String emailToSend) {

        LocalDate today = LocalDate.now();
        List<Object[]> data = salesStockRepository.getTodayTotalSaleGroupedByUsername(today);

        StringBuilder report = new StringBuilder();
        report.append("Today's Sale Report (").append(today).append(")\n\n");

        if (data.isEmpty()) {
            report.append("No sales done today.");
        } else {
            for (Object[] row : data) {
                String username = (String) row[0];
                Double totalSale = (Double) row[1];

                report.append("Username: ").append(username)
                        .append(" | Total Sale: ")
                        .append(String.format("%.2f", totalSale))
                        .append("\n");
            }
        }

        emailService.sendEmail(emailToSend,
                "Today's Total Sale Report",
                report.toString());
    }
}
