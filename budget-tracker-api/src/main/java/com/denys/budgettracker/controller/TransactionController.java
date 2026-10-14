package com.denys.budgettracker.controller;

import com.denys.budgettracker.dto.TransactionRequest;
import com.denys.budgettracker.dto.TransactionResponse;
import com.denys.budgettracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<TransactionResponse> getTransaction() {
        return service.getAllTransactions();
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(@PathVariable Long id){
        return service.getTransactionById(id);
    }

    @PostMapping
    public TransactionResponse createTransaction(@Valid @RequestBody TransactionRequest request){
        return service.createTransaction(request);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id){
        service.deleteTransaction(id);
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request
    ) {
        return service.updateTransaction(id, request);
    }
}
