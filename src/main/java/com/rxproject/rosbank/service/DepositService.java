package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.Deposit;
import com.rxproject.rosbank.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositService {
    
    private final DepositRepository depositRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }
    
    public Deposit getById(Long id){
        return depositRepository.findById(id).orElse(null);
    }
    
    public void save(Deposit deposit){
        depositRepository.save(deposit);
    }
}
