package com.rxproject.rosbank.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

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
    private String view;

    public enum Type {
        BUTTON, // название кнопки
        FORM, // заполненная форма json
        TEXT, // просто текст
        DOC, // Документ загружен
        PHOTO // Фото загружено
    }

    @JsonGetter("message")
    public JsonNode getBodyAsJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(message);
        } catch (IOException e) {
            return null;
        }
    }

    @JsonGetter("view")
    public JsonNode getViewAsJson(){
        if (view == null)
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(view);
        } catch (IOException e) {
            return null;
        }
    }

    @OneToMany(mappedBy = "state")
    Set<Button> buttons;

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
