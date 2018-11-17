package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.Message;
import com.rxproject.rosbank.model.User;
import com.rxproject.rosbank.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    
    public Message getById(Long id){
        return messageRepository.findById(id).orElse(null);
    }
    
    public void save(Message message){
        messageRepository.save(message);
    }

    public List<Message> findByUser(User user){
        return messageRepository.findByUserOrderByTimestampDesc(user);
    }
}
