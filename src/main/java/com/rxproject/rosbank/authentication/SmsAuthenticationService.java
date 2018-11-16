package com.rxproject.rosbank.authentication;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsAuthenticationService {
    private static final ConcurrentHashMap<String, OneTimePassword> cardToOTP = new ConcurrentHashMap<>();

    public Integer create(String cardNo){
        OneTimePassword otp = new OneTimePassword();
        cardToOTP.put(cardNo, otp);
        return otp.getValue();
    }

    public boolean otpIsExpiredOrNotExists(String cardNo){
        OneTimePassword otp = cardToOTP.get(cardNo);
        return otp == null || otp.isExpired();
    }

    public boolean otpIsValid(String cardNo, Integer externalValue){
        OneTimePassword otp = cardToOTP.get(cardNo);
        return otp != null && otp.isValid(externalValue);
    }

    @Getter
    public class OneTimePassword {
        public static final int PIN_CODE_LENGTH = 5; // Symbols for user to input
        public static final int SECONDS_TO_EXPIRE = 60; // One minute for token to expire

        private Integer value;
        private Date expiration;

        OneTimePassword() {
            int a = (int) Math.pow(10, PIN_CODE_LENGTH - 1);
            int b = (int) Math.pow(10, PIN_CODE_LENGTH) - a;
            int rnd = Math.abs(new Random().nextInt());
            value = rnd % b + a;
            expiration = Date.from(Instant.now().plusSeconds(SECONDS_TO_EXPIRE));
        }

        public boolean isExpired() {
            return expiration.before(new Date());
        }

        public boolean isValid(Integer externalValue) {
            return externalValue.equals(value) && !isExpired();
        }
    }
}
