package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.Message;
import com.rxproject.rosbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByUserOrderById(User user);

    Message findLastByUserAndUserMessageIsNullOrderById(User user);

}
