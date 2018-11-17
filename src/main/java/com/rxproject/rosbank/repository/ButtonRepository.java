package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.Account;
import com.rxproject.rosbank.model.Button;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ButtonRepository extends JpaRepository<Button, Long> {

}
