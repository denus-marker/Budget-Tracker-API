package com.denys.budgettracker.dto;

import com.denys.budgettracker.model.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(

    @NotBlank
    @Size(max = 255)
    String description,

    @NotNull
    @Positive
    @Digits(integer = 17, fraction = 2)
    BigDecimal amount,

    @NotNull
    TransactionType type,

    @Size(max = 100)
    String category,

    @NotNull
    LocalDate date
){ }