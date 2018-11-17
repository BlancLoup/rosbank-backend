package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
