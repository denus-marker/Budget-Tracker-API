package com.denys.budgettracker.service;

import com.denys.budgettracker.dto.TransactionRequest;
import com.denys.budgettracker.dto.TransactionResponse;
import com.denys.budgettracker.exception.TransactionNotFoundException;
import com.denys.budgettracker.model.Transaction;
import com.denys.budgettracker.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> getAllTransactions(){
        return transactionRepository.findAll()
                .stream().map(this::toResponse)
                .toList();
    }

    public TransactionResponse getTransactionById(Long id){
        Transaction transaction =   transactionRepository.findById(id)
                .orElseThrow( () -> new TransactionNotFoundException(id));

        return toResponse(transaction);
    }

    public void deleteTransaction(Long id){
        if (!transactionRepository.existsById(id)){
            throw new TransactionNotFoundException(id);
        } else transactionRepository.deleteById(id);
    }

    public TransactionResponse updateTransaction(
            Long id,
            TransactionRequest request
    ) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow( () -> new TransactionNotFoundException(id));

        transaction.setDescription(request.description());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setCategory(request.category());
        transaction.setDate(request.date());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return toResponse(savedTransaction);
    }


    public TransactionResponse createTransaction(TransactionRequest request){
        Transaction transaction = new Transaction();

        transaction.setDescription(request.description());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setCategory(request.category());
        transaction.setDate(request.date());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return toResponse(savedTransaction);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getDate()
        );
    }
}
