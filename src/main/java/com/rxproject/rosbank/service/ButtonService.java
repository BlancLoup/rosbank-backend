package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.Button;
import com.rxproject.rosbank.repository.ButtonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ButtonService {
    
    private final ButtonRepository buttonRepository;

    @Autowired
    public ButtonService(ButtonRepository buttonRepository) {
        this.buttonRepository = buttonRepository;
    }
    
    public Button getById(Long id){
        return buttonRepository.findById(id).orElse(null);
    }
    
    public void save(Button button){
        buttonRepository.save(button);
    }
}
