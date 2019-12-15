package com.mistermicheels.cymi.component.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.common.error.InvalidRequestExceptionType;
import com.mistermicheels.cymi.config.security.SecurityProperties;

@ExtendWith(SpringExtension.class)
public class UserAuthenticationServiceTest {

    @Captor
    ArgumentCaptor<SessionToken> sessionTokenCaptor;

    @Mock
    UserRepository repositoryMock;

    @Mock
    SessionTokenRepository sessionTokenRepositoryMock;

    @Mock
    PasswordService passwordServiceMock;

    @Mock
    SecurityProperties securityPropertiesMock;

    private final int sessionTokenValidityDays = 7;

    private final String email = "email@email.com";
    private final String saltedPasswordHash = "saltedPasswordHash";
    private final String defaultDisplayName = "defaultDisplayName";
    private final String validPassword = "validPassword";
    private final String invalidPassword = "invalidPassword";
    private final LoginData validLoginData = new LoginData(this.email, this.validPassword);
    private final LoginData invalidLoginData = new LoginData(this.email, this.invalidPassword);

    private UserAuthenticationService service;

    @BeforeEach
    public void beforeEach() {
        when(this.passwordServiceMock.isValidPassword(this.validPassword, this.saltedPasswordHash))
                .thenReturn(true);

        when(this.passwordServiceMock.isValidPassword(this.invalidPassword,
                this.saltedPasswordHash)).thenReturn(false);

        when(this.securityPropertiesMock.getSessionTokenValidityDays())
                .thenReturn(this.sessionTokenValidityDays);

        this.service = new UserAuthenticationService(this.repositoryMock,
                this.sessionTokenRepositoryMock, this.passwordServiceMock,
                this.securityPropertiesMock);
    }

    @Test
    public void LoginFailsIfUserNotFound() {
        when(this.repositoryMock.findByEmail(any())).thenReturn(Optional.empty());

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> this.service.getSessionDataForLogin(this.validLoginData));

        assertEquals(InvalidRequestExceptionType.UserNotSignedUp, exception.getType().get());
    }

    @Test
    public void LoginFailsIfUserNotSignedUp() {
        User user = new User(this.email);
        when(this.repositoryMock.findByEmail(any())).thenReturn(Optional.of(user));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> this.service.getSessionDataForLogin(this.validLoginData));

        assertEquals(InvalidRequestExceptionType.UserNotSignedUp, exception.getType().get());
    }

    @Test
    public void LoginFailsIfEmailNotConfirmed() {
        User user = new User(this.email);
        user.signup(this.saltedPasswordHash, this.defaultDisplayName);
        when(this.repositoryMock.findByEmail(any())).thenReturn(Optional.of(user));

        assertThrows(InvalidRequestException.class,
                () -> this.service.getSessionDataForLogin(this.validLoginData));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> this.service.getSessionDataForLogin(this.validLoginData));

        assertEquals(InvalidRequestExceptionType.EmailNotConfirmed, exception.getType().get());
    }

    @Test
    public void LoginFailsForInvalidPassword() {
        User user = new User(this.email);
        user.signup(this.saltedPasswordHash, this.defaultDisplayName);
        user.confirmEmail();
        when(this.repositoryMock.findByEmail(any())).thenReturn(Optional.of(user));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> this.service.getSessionDataForLogin(this.invalidLoginData));

        assertEquals(InvalidRequestExceptionType.InvalidPassword, exception.getType().get());
    }

    @Test
    public void LoginStoresAndReturnsSessionData() {
        User user = new User(this.email);
        user.signup(this.saltedPasswordHash, this.defaultDisplayName);
        user.confirmEmail();
        when(this.repositoryMock.findByEmail(any())).thenReturn(Optional.of(user));

        ZonedDateTime timeBeforeCall = ZonedDateTime.now();
        SessionDataOutgoing sessionData = this.service.getSessionDataForLogin(this.validLoginData);
        ZonedDateTime timeAferCall = ZonedDateTime.now();

        verify(this.sessionTokenRepositoryMock).save(this.sessionTokenCaptor.capture());
        SessionToken storedSessionToken = this.sessionTokenCaptor.getValue();

        assertEquals(sessionData.getSessionToken(), storedSessionToken.getId());
        assertEquals(sessionData.getCsrfToken(), storedSessionToken.getCsrfToken());

        assertTrue(timeBeforeCall.plusDays(this.sessionTokenValidityDays)
                .isBefore(storedSessionToken.getExpirationTimestamp()));

        assertTrue(timeAferCall.plusDays(this.sessionTokenValidityDays)
                .isAfter(storedSessionToken.getExpirationTimestamp()));
    }

}
