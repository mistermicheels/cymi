package com.mistermicheels.cymi.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.security")
@ConstructorBinding
@Validated
public class SecurityProperties {
    
    private final int sessionTokenValidityDays = 7;
    private final String sessionTokenCookieName = "SESSION-TOKEN";
    private final String csrfTokenCookieName = "XSRF-TOKEN";
    private final String csrfTokenHeaderName = "X-XSRF-TOKEN";
    
    private final int emailConfirmationTokenValidityDays = 7;
    
    public SecurityProperties() {
    }

    public int getSessionTokenValidityDays() {
        return sessionTokenValidityDays;
    }

    public String getSessionTokenCookieName() {
        return sessionTokenCookieName;
    }

    public String getCsrfTokenCookieName() {
        return csrfTokenCookieName;
    }

    public String getCsrfTokenHeaderName() {
        return csrfTokenHeaderName;
    }    

    public int getEmailConfirmationTokenValidityDays() {
        return emailConfirmationTokenValidityDays;
    }

}
