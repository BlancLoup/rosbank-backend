package com.rxproject.rosbank.authentication;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public final class Authority implements GrantedAuthority {
    private static final String ROLE_PREFIX = "ROLE_";

    public static final Authority USER = new Authority("USER");
    
    private String roleName;
    private Authority(String roleName) {
        this.roleName = roleName;
    }
    
    @Override
    public String getAuthority() {
        return ROLE_PREFIX + roleName;
    }
}
