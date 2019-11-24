package com.mistermicheels.cymi.config.security;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class CustomAuthenticationException extends AuthenticationException {

    public CustomAuthenticationException(String msg) {
        super(msg);
    }

    public CustomAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

}
