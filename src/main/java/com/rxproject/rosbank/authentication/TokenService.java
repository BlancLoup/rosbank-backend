package com.rxproject.rosbank.authentication;

import com.rxproject.rosbank.model.User;
import com.rxproject.rosbank.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class TokenService {

    private final TokenHandler tokenHandler;
    private final UserService userService;

    @Autowired
    public TokenService(TokenHandler tokenHandler, UserService userService) {
        this.tokenHandler = tokenHandler;
        this.userService = userService;
    }
    
    public Optional<Authentication> authenticate(String accessToken){
        Claims body = tokenHandler.extractInfo(accessToken);
        if (body != null &&
                body.getSubject().equals("access") &&
                body.getExpiration().after(new Date())) {
            if (body.getAudience().equals(Authority.USER.getRoleName())) {
                User user = userService.getById(Long.parseLong(body.getId()));
                return Optional.of(new TokenAuthentication(user, Authority.USER));
            }
        }
        return Optional.empty();
    }

    public Token generateToken(User user){
        return tokenHandler.generateToken(Authority.USER.getRoleName(), user.getId());
    }
}

