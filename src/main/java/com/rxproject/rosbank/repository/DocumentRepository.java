package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
