package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.DialogState;
import com.rxproject.rosbank.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
