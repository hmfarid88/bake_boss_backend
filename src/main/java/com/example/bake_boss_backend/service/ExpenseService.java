package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.Expense;
import com.example.bake_boss_backend.repository.ExpenseRepository;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getCurrentMonthExpenses(String username) {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        return expenseRepository.findByMonthYearAndUsername(currentMonth, currentYear, username);
    }

    public List<Expense> getDatewiseExpenses(String username, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findDatewiseExpense(username, startDate, endDate);
    }
}
