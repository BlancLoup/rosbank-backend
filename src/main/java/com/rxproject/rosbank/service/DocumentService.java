package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.Document;
import com.rxproject.rosbank.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document getById(Long id){
        return documentRepository.findById(id).orElse(null);
    }

    public void save(Document document){
        documentRepository.save(document);
    }
}
