package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.DialogState;
import com.rxproject.rosbank.repository.DialogStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DialogStateService {
    
    private final DialogStateRepository dialogStateRepository;

    @Autowired
    public DialogStateService(DialogStateRepository dialogStateRepository) {
        this.dialogStateRepository = dialogStateRepository;
    }
    
    public DialogState getById(Long id){
        return dialogStateRepository.findById(id).orElse(null);
    }
    
    public void save(DialogState dialogState){
        dialogStateRepository.save(dialogState);
    }
}
