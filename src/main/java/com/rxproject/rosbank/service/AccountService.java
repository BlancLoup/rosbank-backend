package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.Account;
import com.rxproject.rosbank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getById(Long id){
        return accountRepository.findById(id).orElse(null);
    }

    public void save(Account account){
        accountRepository.save(account);
    }
}
