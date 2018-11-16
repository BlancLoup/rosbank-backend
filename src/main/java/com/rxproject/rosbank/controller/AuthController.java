package com.rxproject.rosbank.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rxproject.rosbank.authentication.SmsAuthenticationService;
import com.rxproject.rosbank.authentication.Token;
import com.rxproject.rosbank.authentication.TokenService;
import com.rxproject.rosbank.model.User;
import com.rxproject.rosbank.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Log4j
public class AuthController {

    private final UserService userService;
    private final SmsAuthenticationService smsAuthenticationService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(UserService userService, SmsAuthenticationService smsAuthenticationService, TokenService tokenService) {
        this.userService = userService;
        this.smsAuthenticationService = smsAuthenticationService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    public ResponseEntity loginUser(@RequestBody JsonNode json){
        JsonNodeFactory jnf = JsonNodeFactory.instance;
        if (!json.hasNonNull("cardNumber")){
            ObjectNode res = jnf.objectNode();
            res.put("message", "Card number must be present");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        String cardNo = json.get("cardNumber").asText();
        User user = userService.findByCardNo(cardNo);
        if (user == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        log.info(String.format("Got login request for %s", cardNo));
        if (json.hasNonNull("otp")){
            Integer gotOtp = json.get("otp").asInt();
            if (smsAuthenticationService.otpIsValid(cardNo, gotOtp)) {
                Token token = tokenService.generateToken(user);
                log.info("Login success");
                return new ResponseEntity<>(token, HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        else {
            if (smsAuthenticationService.otpIsExpiredOrNotExists(cardNo)) {
                Integer otp = smsAuthenticationService.create(cardNo);
                ObjectNode res = jnf.objectNode();
                res.put("otp", otp);
                log.info("OTP sent");
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
            }
        }
    }
}
