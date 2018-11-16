package com.rxproject.rosbank.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "credits")
@Getter
@Setter
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // Процент по кредиту
    @Column(name = "percent")
    private Double percent;

    // Просрочка по кредиту
    @Column(name = "overdue")
    private Double overdue;

    // Начальная сумма
    @Column(name = "start_sum")
    private Double startSum;

    // Остаток по кредиту
    @Column(name = "debt")
    private Double debt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credit)) return false;
        Credit credit = (Credit) o;
        return Objects.equals(id, credit.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
