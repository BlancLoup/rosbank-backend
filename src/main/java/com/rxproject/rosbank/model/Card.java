package com.rxproject.rosbank.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // Имя банка
    @Column(name = "bank_name")
    private String bankName;

    // Описание типа карты
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private CardType cardType;

    // Состояние карты
    private boolean blocked;

    // состояние Apple Pay
    @Column(name = "apple_pay_allowed")
    private boolean applePayAllowed;

    // Имя карты, которое задал пользователь
    @Column(name = "user_name")
    private String userName;

    // Платежная система
    @Column(name = "pay_system")
    private String paySystem;

    // Номер карты
    @Column(name = "card_number")
    private String cardNumber;

    // Имя держателя карты
    @Column(name = "holder_name")
    private String holderName;

    // Процент по кредитной карте
    @Column(name = "percent")
    private Double percent;

    // Просрочка по кредитной карте
    @Column(name = "overdue")
    private Double overdue;

    // Срок беспроцентного пользования кредитным лимитом
    @Column(name = "free_limit")
    private Double freeLimit;

    // Срок беспроцентного пользования кредитным лимитом
    @Column(name = "credit_limit")
    private Double creditLimit;

    public enum CardType {
        DEBIT,
        CREDIT,
        OVERDRAFT,
        CORPORATE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
