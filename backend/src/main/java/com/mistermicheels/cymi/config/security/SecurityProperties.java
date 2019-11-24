package com.mistermicheels.cymi.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.security")
@Validated
public class SecurityProperties {
    
    private int sessionTokenValidityDays = 7;
    private String sessionTokenCookieName = "SESSION-TOKEN";
    private String csrfTokenCookieName = "XSRF-TOKEN";
    private String csrfTokenHeaderName = "X-XSRF-TOKEN";
    
    private int emailConfirmationTokenValidityDays = 7;

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

    void setSessionTokenValidityDays(int sessionTokenValidityDays) {
        this.sessionTokenValidityDays = sessionTokenValidityDays;
    }

    void setSessionTokenCookieName(String sessionTokenCookieName) {
        this.sessionTokenCookieName = sessionTokenCookieName;
    }

    void setCsrfTokenCookieName(String csrfTokenCookieName) {
        this.csrfTokenCookieName = csrfTokenCookieName;
    }

    void setCsrfTokenHeaderName(String csrfTokenHeaderName) {
        this.csrfTokenHeaderName = csrfTokenHeaderName;
    }

    void setEmailConfirmationTokenValidityDays(int emailConfirmationTokenValidityDays) {
        this.emailConfirmationTokenValidityDays = emailConfirmationTokenValidityDays;
    }

}
