package com.rxproject.rosbank.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Номер счета
    @Column(name = "account_number")
    private String accountNumber;

    // Баланс счета
    @Column(name = "balance")
    private Double balance;

    // Валюта
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    // Дата открытия счета
    @Column(name = "start_date")
    private Date startDate;

    // Дата окончания действия счета
    @Column(name = "exp_date")
    private Date expDate;

    // Номер счета
    @Column(name = "bic")
    private String bic;

    // Номер счета
    @Column(name = "inn")
    private String inn;

    // Информация о тарифе
    @Column(name = "tariff")
    private String tariff;

    @Column(name = "invest")
    private boolean invest;

    public enum Currency{
        RUR,
        USD,
        EUR,
        GBP,
        JPY,
        A33, //палладий
        A76, //платина
        A98, //золото
        A99 //серебро
    }

    @JsonGetter("startDate")
    public long getStartDateJson(){
        return startDate.getTime();
    }

    @JsonGetter("expDate")
    public long getExpDateJson(){
        return expDate.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
