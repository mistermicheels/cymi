package com.mistermicheels.cymi.component.user;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.component.user.entity.SessionToken;
import com.mistermicheels.cymi.component.user.entity.User;

public class SessionTokenIncomingTest {

    User userMock = Mockito.mock(User.class);

    private final String validSessionToken = "validSessionToken";
    private final String invalidSessionToken = "invalidSessionToken";
    private final String validCsrfToken = "validCsrfToken";
    private final String invalidCsrfToken = "invalidCsrfToken";

    private final SessionToken validTokenObject = new SessionToken(this.validSessionToken,
            this.userMock, ZonedDateTime.now().plusHours(1), this.validCsrfToken);

    @Test()
    public void StateChangingCheckFailsForInvalidSessionToken() {
        SessionDataIncoming sessionDataIncoming = SessionDataIncoming
                .forStateChangingRequest(this.invalidSessionToken, this.validCsrfToken);

        assertThrows(InvalidRequestException.class,
                () -> sessionDataIncoming.checkValidityAgainstToken(this.validTokenObject));
    }

    @Test
    public void StateChangingCheckFailsForInvalidCsrfToken() {
        SessionDataIncoming sessionDataIncoming = SessionDataIncoming
                .forStateChangingRequest(this.validSessionToken, this.invalidCsrfToken);

        assertThrows(InvalidRequestException.class,
                () -> sessionDataIncoming.checkValidityAgainstToken(this.validTokenObject));
    }

    @Test
    public void StateChangingCheckSucceedsForValidTokens() {
        SessionDataIncoming sessionDataIncoming = SessionDataIncoming
                .forStateChangingRequest(this.validSessionToken, this.validCsrfToken);

        sessionDataIncoming.checkValidityAgainstToken(this.validTokenObject);
    }

    @Test()
    public void NonStateChangingCheckFailsForInvalidSessionToken() {
        SessionDataIncoming sessionDataIncoming = SessionDataIncoming
                .forNonStateChangingRequest(this.invalidSessionToken);

        assertThrows(InvalidRequestException.class,
                () -> sessionDataIncoming.checkValidityAgainstToken(this.validTokenObject));
    }

    @Test()
    public void NonStateChangingCheckSucceedsForValidSessionToken() {
        SessionDataIncoming sessionDataIncoming = SessionDataIncoming
                .forNonStateChangingRequest(this.validSessionToken);

        sessionDataIncoming.checkValidityAgainstToken(this.validTokenObject);
    }

}
