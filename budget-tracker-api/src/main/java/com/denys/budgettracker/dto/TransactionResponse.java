package com.denys.budgettracker.dto;

import com.denys.budgettracker.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse (
        Long id,
        String description,
        BigDecimal amount,
        TransactionType type,
        String category,
        LocalDate date
){ }