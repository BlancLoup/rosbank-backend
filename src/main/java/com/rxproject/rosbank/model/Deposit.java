package com.rxproject.rosbank.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "deposits")
@Getter
@Setter
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "percent")
    private Double percent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deposit)) return false;
        Deposit deposit = (Deposit) o;
        return Objects.equals(id, deposit.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
