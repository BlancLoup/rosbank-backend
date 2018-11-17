package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.Transaction;
import com.rxproject.rosbank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    public Transaction getById(Long id){
        return transactionRepository.findById(id).orElse(null);
    }
    
    public void save(Transaction transaction){
        transactionRepository.save(transaction);
    }
}
