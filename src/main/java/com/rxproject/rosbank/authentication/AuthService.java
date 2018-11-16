package com.rxproject.rosbank.authentication;

import com.rxproject.rosbank.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public User getUser() {
        Object possiblyUser = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (possiblyUser instanceof User)
            return (User) possiblyUser;
        return null;
    }
}
