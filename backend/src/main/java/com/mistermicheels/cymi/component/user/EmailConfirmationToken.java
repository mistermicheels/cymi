package com.mistermicheels.cymi.component.user;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "user_email_confirmation_token", indexes = {
        @Index(name = "user_email_confirmation_token_expiration_timestamp_idx", columnList = "expirationTimestamp") })
class EmailConfirmationToken extends UserTokenBase {

    EmailConfirmationToken() {
        super();
    }

    EmailConfirmationToken(String token, User user, ZonedDateTime expirationTimestamp) {
        super(token, user, expirationTimestamp);
    }

}
