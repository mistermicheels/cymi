package com.mistermicheels.cymi.component.user;

import java.util.Objects;

import com.mistermicheels.cymi.common.error.InvalidRequestException;

public class SessionDataIncoming {

    private final String sessionToken;
    private final String csrfToken;
    private final boolean isStateChangingRequest;

    public static SessionDataIncoming forStateChangingRequest(String sessionToken,
            String csrfToken) {
        return new SessionDataIncoming(sessionToken, csrfToken, true);
    }

    public static SessionDataIncoming forNonStateChangingRequest(String sessionToken) {
        return new SessionDataIncoming(sessionToken, null, false);
    }

    private SessionDataIncoming(String sessionToken, String csrfToken,
            boolean isStateChangingRequest) {
        this.sessionToken = sessionToken;
        this.csrfToken = csrfToken;
        this.isStateChangingRequest = isStateChangingRequest;
    }

    public void checkValidityAgainstToken(SessionToken token) {
        if (!Objects.equals(this.sessionToken, token.getId())) {
            throw new InvalidRequestException("Invalid session token");
        } else if (token.hasExpired()) {
            throw new InvalidRequestException("Expired token");
        }

        boolean isValidCsrfToken = !this.isStateChangingRequest
                || Objects.equals(this.csrfToken, token.getCsrfToken());

        if (!isValidCsrfToken) {
            throw new InvalidRequestException("Invalid CSRF token");
        }
    }

    public String getSessionToken() {
        return this.sessionToken;
    }

}
