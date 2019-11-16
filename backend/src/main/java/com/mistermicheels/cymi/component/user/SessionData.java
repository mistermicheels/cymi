package com.mistermicheels.cymi.component.user;

public class SessionData {

    private final String sessionToken;
    private final String csrfToken;

    public SessionData(String sessionToken, String csrfToken) {
        this.sessionToken = sessionToken;
        this.csrfToken = csrfToken;
    }

    public String getSessionToken() {
        return this.sessionToken;
    }

    public String getCsrfToken() {
        return this.csrfToken;
    }

}
