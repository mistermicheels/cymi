package com.mistermicheels.cymi.component.user;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "user_session_token", indexes = {
        @Index(name = "user_session_token_expiration_timestamp_idx", columnList = "expirationTimestamp") })
class SessionToken extends UserTokenBase {

    @Column(nullable = false)
    private String csrfToken;

    SessionToken() {
        super();
    }

    SessionToken(String token, User user, ZonedDateTime expirationTimestamp, String csrfToken) {
        super(token, user, expirationTimestamp);
        this.csrfToken = csrfToken;
    }

    public String getCsrfToken() {
        return this.csrfToken;
    }

}
