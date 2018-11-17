package com.rxproject.rosbank.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "dialog_states")
@Getter
@Setter
public class DialogState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @Column(name = "endpoint")
    private String endpoint;

    @Transient
    private JsonNode view;

    public enum Type {
        BUTTON,
        FORM,
        TEXT,
        DOC,
        PHOTO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DialogState)) return false;
        DialogState that = (DialogState) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
