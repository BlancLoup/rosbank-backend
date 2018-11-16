package com.rxproject.rosbank.repository;

import com.rxproject.rosbank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCardNumber(String cardNo);

}
