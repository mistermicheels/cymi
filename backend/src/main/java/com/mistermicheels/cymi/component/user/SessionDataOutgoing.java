package com.mistermicheels.cymi.component.user;

public class SessionDataOutgoing {

    private final String sessionToken;
    private final String csrfToken;
    private final User user;

    public SessionDataOutgoing(String sessionToken, String csrfToken, User user) {
        this.sessionToken = sessionToken;
        this.csrfToken = csrfToken;
        this.user = user;
    }

    public String getSessionToken() {
        return this.sessionToken;
    }

    public String getCsrfToken() {
        return this.csrfToken;
    }

    public User getUser() {
        return this.user;
    }

}
