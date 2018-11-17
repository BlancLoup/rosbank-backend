package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

}
