package com.rxproject.rosbank.controller;
import com.rxproject.rosbank.authentication.AuthService;
import com.rxproject.rosbank.model.User;
import com.rxproject.rosbank.service.CardService;
import com.rxproject.rosbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthService authService;

    @Autowired
    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getUser(){
        User user = authService.getUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "cards", method = RequestMethod.GET)
    public ResponseEntity getCards(){
        User user = authService.getUser();
        return new ResponseEntity<>(user.getCards(), HttpStatus.OK);
    }
}
