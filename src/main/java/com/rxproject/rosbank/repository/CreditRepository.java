package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.Button;
import com.rxproject.rosbank.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {

}
