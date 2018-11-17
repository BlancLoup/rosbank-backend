package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.Credit;
import com.rxproject.rosbank.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditService {
    
    private final CreditRepository creditRepository;

    @Autowired
    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }
    
    public Credit getById(Long id){
        return creditRepository.findById(id).orElse(null);
    }
    
    public void save(Credit credit){
        creditRepository.save(credit);
    }
}
