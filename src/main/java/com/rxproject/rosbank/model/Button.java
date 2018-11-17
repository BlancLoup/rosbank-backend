package com.rxproject.rosbank.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "buttons")
@Getter
@Setter
public class Button {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "state_id")
    @JsonIgnore
    private DialogState state;

    @ManyToOne
    @JoinColumn(name = "target_state_id")
    @JsonIgnore
    private DialogState targetState;

    @Column(name = "endpoint")
    private String endpoint;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Button)) return false;
        Button button = (Button) o;
        return Objects.equals(id, button.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
