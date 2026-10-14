package com.denys.budgettracker.repository;

import com.denys.budgettracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {
}
