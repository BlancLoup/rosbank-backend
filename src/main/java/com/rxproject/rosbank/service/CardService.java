package com.rxproject.rosbank.service;

import com.rxproject.rosbank.model.Card;
import com.rxproject.rosbank.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card getById(Long id){
        return cardRepository.findById(id).orElse(null);
    }

    public Card findByCardNo(String cardNo){
        return cardRepository.findByCardNumber(cardNo);
    }

    public void save(Card card) {
        cardRepository.save(card);
    }
}
