package com.rxproject.rosbank.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {

    public Message(User user){
        this.user = user;
        this.timestamp = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "body")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "timestamp")
    private Date timestamp;

    public enum Type{
        USER,
        BOT
    }

    @JsonGetter("body")
    public JsonNode getBodyAsJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(body);
        } catch (IOException e) {
            return null;
        }
    }

    @JsonGetter("timestamp")
    public long getTimestampJson(){
        return timestamp.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
