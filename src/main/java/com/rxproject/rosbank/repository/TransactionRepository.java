package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.PartnerOffer;
import com.rxproject.rosbank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
