package com.rxproject.rosbank.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rxproject.rosbank.utils.UserMessageWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    public Message(User user){
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "bot_time")
    private Date botTime;

    @Column(name = "bot_message")
    private String botMessage;

    @Column(name = "user_time")
    private Date userTime;

    @Column(name = "user_message")
    private String userMessage;

    @ManyToOne
    @JoinColumn(name = "state_id")
    @JsonIgnore
    private DialogState state;


    @JsonGetter("botMessage")
    public JsonNode getBotMessageAsJson(){
        if (botMessage == null)
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(botMessage);
        } catch (IOException e) {
            return null;
        }
    }

    @JsonGetter("userMessage")
    public JsonNode getUserMessageAsJson(){
        if (userMessage == null)
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(userMessage);
        } catch (IOException e) {
            return null;
        }
    }

    @JsonGetter("botTime")
    public Long getBotTimeJson(){
        return Optional.ofNullable(botTime).map(Date::getTime).orElse(null);
    }

    @JsonGetter("userTime")
    public Long getUserTimeJson(){
        return Optional.ofNullable(userTime).map(Date::getTime).orElse(null);
    }

    public void addBotMessage(DialogState dialogState){
        this.botTime = new Date();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.botMessage = objectMapper.writeValueAsString(dialogState);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void addUserAnswer(UserMessageWrapper umv){
        this.userTime = new Date();
        this.userMessage = umv.toJSON();
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
