package com.rxproject.rosbank.service;

import com.rxproject.rosbank.model.User;
import com.rxproject.rosbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByCardNo(String cardNo){
        return userRepository.findByCardNo(cardNo);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
