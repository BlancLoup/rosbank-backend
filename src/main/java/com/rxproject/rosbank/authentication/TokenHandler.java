package com.rxproject.rosbank.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenHandler {

    private final SecretKey SECRET_KEY;

    public TokenHandler() {
        String jwtKey = "ul7r453cr37r05b4nkrxpr0j3c7jw74ppl1c4710nk3y";
        byte[] encodedKey = Base64.getEncoder().encode(jwtKey.getBytes());
        SECRET_KEY = new SecretKeySpec(encodedKey, 0, encodedKey.length,"AES");
    }

    public Claims extractInfo(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            if (body.getId() != null &&
                    body.getAudience() != null &&
                    body.getSubject() != null &&
                    body.getExpiration() != null){
                return body;
            }
            return null;
        }
        catch (RuntimeException ex){
            return null;
        }
    }

    public Token generateToken(String role, String id) {
        int accessExpiration = 6*60*60;
        Date accessTokenExpiration = Date.from(Instant.now().plusSeconds(accessExpiration));
        String accessToken = Jwts.builder()
                .setSubject("access")
                .setId(id)
                .setAudience(role)
                .setExpiration(accessTokenExpiration)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        return new Token(accessToken, accessTokenExpiration.getTime());
    }

    public Token generateToken(String subject, Long id){
        return generateToken(subject, id.toString());
    }

}
