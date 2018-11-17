package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.Deposit;
import com.rxproject.rosbank.model.DialogState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DialogStateRepository extends JpaRepository<DialogState, Long> {

}
