package com.denys.budgettracker.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Transaction with id: " + id + " not found.");
    }
}
