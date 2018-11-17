package com.rxproject.rosbank.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rxproject.rosbank.authentication.AuthService;
import com.rxproject.rosbank.model.*;
import com.rxproject.rosbank.service.*;
import com.rxproject.rosbank.utils.MessageWrapper;
import com.rxproject.rosbank.utils.ViewWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@Transactional
public class UserController {

    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final UserService userService;
    private final ButtonService buttonService;
    private final DialogStateService dialogStateService;
    private final MessageService messageService;
    private final DocumentService documentService;


    @Autowired
    public UserController(ObjectMapper objectMapper, AuthService authService, UserService userService, ButtonService buttonService, DialogStateService dialogStateService, MessageService messageService, DocumentService documentService) {
        this.objectMapper = objectMapper;
        this.authService = authService;
        this.userService = userService;
        this.buttonService = buttonService;
        this.dialogStateService = dialogStateService;
        this.messageService = messageService;
        this.documentService = documentService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getUser(){
        User user = authService.getUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "ds", method = RequestMethod.GET)
    public ResponseEntity getUserDialogState(){
        User user = authService.getUser();
        return new ResponseEntity<>(user.getState(), HttpStatus.OK);
    }

    @RequestMapping(value = "history", method = RequestMethod.GET)
    public ResponseEntity getMessageHistory(){
        User user = authService.getUser();
        return new ResponseEntity<>(messageService.findByUser(user), HttpStatus.OK);
    }

    @RequestMapping(value = "cards", method = RequestMethod.POST)
    public ResponseEntity getCards(){
        User user = authService.getUser();
        if (user.getState().getId() != 1L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        DialogState targetState = dialogStateService.getById(4L);
        user.setState(targetState);
        userService.save(user);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String rawJson = objectMapper.writeValueAsString(user.getCards());
            ViewWrapper viewWrapper = new ViewWrapper();
            viewWrapper.setType(ViewWrapper.Type.CARDS);
            viewWrapper.setContent(objectMapper.readTree(rawJson));
            targetState.setView(viewWrapper);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    @RequestMapping(value = "buttons", method = RequestMethod.POST)
    public ResponseEntity buttonHandler(@RequestBody Button bodyBtn){
        Button button = buttonService.getById(bodyBtn.getId());
        if (button == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        User user = authService.getUser();
        DialogState currentState = user.getState();
        Set<Button> buttons = currentState.getButtons();
        if (!buttons.contains(button))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        DialogState targetState = button.getTargetState();
        Message message = new Message(user);
        message.setType(Message.Type.USER);
        try {
            message.setBody(objectMapper.writeValueAsString(new MessageWrapper(MessageWrapper.Type.BUTTON, button.getText())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        message = new Message(user);
        message.setType(Message.Type.BOT);
        try {
            message.setBody(objectMapper.writeValueAsString(targetState));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messageService.save(message);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    @RequestMapping(value ="textstate", method = RequestMethod.POST)
    public ResponseEntity textHandler(@RequestBody JsonNode json){
        String text = json.get("text").asText();
        User user = authService.getUser();
        Message message = new Message(user);
        message.setBody(text);
        message.setType(Message.Type.USER);
        messageService.save(message);
        DialogState targetState = dialogStateService.getById(1L);
        message = new Message(user);
        message.setType(Message.Type.BOT);
        try {
            message.setBody(objectMapper.writeValueAsString(targetState));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState,HttpStatus.OK);
    }

    @RequestMapping(value ="photostate", method = RequestMethod.POST)
    public ResponseEntity photoHandler(@RequestParam("file") MultipartFile file){
        User user = authService.getUser();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String originalFilename = file.getOriginalFilename();
                String fileName = String.format("user_files/%d-%s",new Date().getTime(), originalFilename);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                Document document = new Document();
                document.setPath(fileName);
                document.setUser(user);
                documentService.save(document);
                Message message = new Message(user);
                message.setBody(fileName);
                message.setType(Message.Type.USER);
                messageService.save(message);
                DialogState targetState = dialogStateService.getById(1L);
                message = new Message(user);
                message.setType(Message.Type.BOT);
                try {
                    message.setBody(objectMapper.writeValueAsString(targetState));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                messageService.save(message);
                user.setState(targetState);
                userService.save(user);
                return new ResponseEntity<>(targetState, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value ="docstate", method = RequestMethod.POST)
    public ResponseEntity docHandler(@RequestParam("file") MultipartFile file){
        User user = authService.getUser();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String originalFilename = file.getOriginalFilename();
                String fileName = String.format("user_files/%d-%s",new Date().getTime(), originalFilename);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                Message message = new Message(user);
                message.setBody(fileName);
                message.setType(Message.Type.USER);
                messageService.save(message);
                Document document = new Document();
                document.setPath(fileName);
                document.setUser(user);
                documentService.save(document);
                DialogState targetState = dialogStateService.getById(1L);
                message = new Message(user);
                message.setType(Message.Type.BOT);
                try {
                    message.setBody(objectMapper.writeValueAsString(targetState));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                messageService.save(message);
                user.setState(targetState);
                userService.save(user);
                return new ResponseEntity<>(targetState, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
