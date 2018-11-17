package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.Form;
import com.rxproject.rosbank.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormService {
    
    private final FormRepository formRepository;

    @Autowired
    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }
    
    public Form getById(Long id){
        return formRepository.findById(id).orElse(null);
    }
    
    public void save(Form form){
        formRepository.save(form);
    }
}
