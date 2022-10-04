package com.mistermicheels.cymi.component.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.common.error.InvalidRequestExceptionType;
import com.mistermicheels.cymi.component.user.entity.EmailConfirmationToken;
import com.mistermicheels.cymi.component.user.entity.User;
import com.mistermicheels.cymi.config.security.SecurityProperties;
import com.mistermicheels.cymi.io.email.EmailService;
import com.mistermicheels.cymi.io.email.emailMessage.ConfirmEmailEmailMessage;

@ExtendWith(SpringExtension.class)
public class UserSignupServiceTest {

    ArgumentCaptor<EmailConfirmationToken> confirmationTokenCaptor = ArgumentCaptor
            .forClass(EmailConfirmationToken.class);

    ArgumentCaptor<ConfirmEmailEmailMessage> confirmationMessageCaptor = ArgumentCaptor
            .forClass(ConfirmEmailEmailMessage.class);

    UserRepository repositoryMock = Mockito.mock(UserRepository.class);

    EmailConfirmationTokenRepository emailConfirmationTokenRepositoryMock = Mockito
            .mock(EmailConfirmationTokenRepository.class);

    PasswordService passwordServiceMock = Mockito.mock(PasswordService.class);
    EmailService emailServiceMock = Mockito.mock(EmailService.class);
    SecurityProperties securityPropertiesMock = Mockito.mock(SecurityProperties.class);

    private final int emailConfirmationTokenValidityDays = 7;

    private final String email = "email@email.com";
    private final String password = "password";
    private final LoginData loginData = new LoginData(this.email, this.password);
    private final String defaultDisplayName = "defaultDisplayName";
    private final String emailConfirmationToken = "emailConfirmationToken";

    private final Long userId = 1L;
    private final User userForConfirmationToken = User.forTestWithId(this.userId);
    private final User invitedWithEmail = new User(this.email);
    private final User signedUpWithEmail = new User(this.email);

    private UserSignupService service = new UserSignupService(this.repositoryMock,
            this.emailConfirmationTokenRepositoryMock, this.passwordServiceMock,
            this.emailServiceMock, this.securityPropertiesMock);

    @BeforeEach
    public void beforeEach() {
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setIdForTest(this.userId);
            return user;
        }).when(this.repositoryMock).saveAndFlush(any());

        when(this.securityPropertiesMock.getEmailConfirmationTokenValidityDays())
                .thenReturn(this.emailConfirmationTokenValidityDays);

        this.invitedWithEmail.setIdForTest(this.userId);

        this.signedUpWithEmail.setIdForTest(this.userId);
        this.signedUpWithEmail.signup("saltedPasswordHash", this.defaultDisplayName);
    }

    @Test
    public void SignupFailsIfEmailAreadySignedUp() {
        when(this.repositoryMock.findByEmail(this.email))
                .thenReturn(Optional.of(this.signedUpWithEmail));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> this.service.signUpUser(this.loginData, this.defaultDisplayName));

        assertEquals(InvalidRequestExceptionType.EmailAlreadyTaken, exception.getType().get());
    }

    @Test
    public void SignupSendsAndStoresEmailConfirmationTokenForNewEmail() {
        when(this.repositoryMock.findByEmail(this.email)).thenReturn(Optional.empty());

        ZonedDateTime timeBeforeCall = ZonedDateTime.now();
        this.service.signUpUser(this.loginData, this.defaultDisplayName);
        ZonedDateTime timeAferCall = ZonedDateTime.now();

        verify(this.emailConfirmationTokenRepositoryMock)
                .save(this.confirmationTokenCaptor.capture());

        EmailConfirmationToken storedConfirmationToken = this.confirmationTokenCaptor.getValue();

        verify(this.emailServiceMock).send(this.confirmationMessageCaptor.capture());
        ConfirmEmailEmailMessage confirmationMessage = this.confirmationMessageCaptor.getValue();

        assertEquals(this.userId, storedConfirmationToken.getUserId());

        assertEquals(storedConfirmationToken.getId(),
                confirmationMessage.getEmailConfirmationToken());

        assertTrue(timeBeforeCall.plusDays(this.emailConfirmationTokenValidityDays)
                .isBefore(storedConfirmationToken.getExpirationTimestamp()));

        assertTrue(timeAferCall.plusDays(this.emailConfirmationTokenValidityDays)
                .isAfter(storedConfirmationToken.getExpirationTimestamp()));
    }

    @Test
    public void SignupAcceptsValidEmailConfirmationTokenForExistingEmail() {
        when(this.repositoryMock.findByEmail(this.email))
                .thenReturn(Optional.of(this.invitedWithEmail));

        ZonedDateTime expirationTimestamp = ZonedDateTime.now().plusDays(1);

        EmailConfirmationToken validToken = new EmailConfirmationToken(this.emailConfirmationToken,
                this.invitedWithEmail, expirationTimestamp);

        when(this.emailConfirmationTokenRepositoryMock.findById(any()))
                .thenReturn(Optional.of(validToken));

        this.service.signUpUser(this.loginData, this.defaultDisplayName, validToken.getId());

        verify(this.emailConfirmationTokenRepositoryMock, never()).save(any());
        verify(this.emailServiceMock, never()).send(any());
    }

    @Test
    public void EmailConfirmationFailsForNonExistingToken() {
        when(this.emailConfirmationTokenRepositoryMock.findByIdWithUser(any()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class,
                () -> this.service.confirmEmail(this.emailConfirmationToken));
    }

    @Test
    public void EmailConfirmationFailsForExpiredToken() {
        ZonedDateTime expirationTimestamp = ZonedDateTime.now();

        EmailConfirmationToken expiredToken = new EmailConfirmationToken(
                this.emailConfirmationToken, this.userForConfirmationToken, expirationTimestamp);

        when(this.emailConfirmationTokenRepositoryMock.findByIdWithUser(any()))
                .thenReturn(Optional.of(expiredToken));

        assertThrows(InvalidRequestException.class,
                () -> this.service.confirmEmail(this.emailConfirmationToken));
    }

    @Test
    public void EmailConfirmationSucceedsForValidInput() {
        ZonedDateTime expirationTimestamp = ZonedDateTime.now().plusDays(1);

        EmailConfirmationToken validToken = new EmailConfirmationToken(this.emailConfirmationToken,
                this.userForConfirmationToken, expirationTimestamp);

        when(this.emailConfirmationTokenRepositoryMock.findByIdWithUser(any()))
                .thenReturn(Optional.of(validToken));

        assertFalse(this.userForConfirmationToken.isEmailConfirmed());
        this.service.confirmEmail(this.emailConfirmationToken);
        assertTrue(this.userForConfirmationToken.isEmailConfirmed());
    }

}
