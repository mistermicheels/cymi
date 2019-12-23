package com.mistermicheels.cymi.component.user;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.common.error.InvalidRequestExceptionType;
import com.mistermicheels.cymi.config.security.SecurityProperties;

@Service
class UserAuthenticationService {

    private final UserRepository repository;
    private final SessionTokenRepository sessionTokenRepository;
    private final PasswordService passwordService;

    private final int sessionTokenValidityDays;

    @Autowired
    UserAuthenticationService(UserRepository repository,
            SessionTokenRepository sessionTokenRepository, PasswordService passwordService,
            SecurityProperties securityProperties) {
        this.repository = repository;
        this.sessionTokenRepository = sessionTokenRepository;
        this.passwordService = passwordService;
        this.sessionTokenValidityDays = securityProperties.getSessionTokenValidityDays();
    }

    public SessionDataOutgoing getSessionDataForLogin(LoginData loginData) {
        String signupMessage = "Please sign up first";

        User user = this.repository.findByEmail(loginData.getEmailLowerCase())
                .orElseThrow(() -> new InvalidRequestException(signupMessage,
                        InvalidRequestExceptionType.UserNotSignedUp));

        String saltedPasswordHash = user.getSaltedPasswordHash()
                .orElseThrow(() -> new InvalidRequestException(signupMessage,
                        InvalidRequestExceptionType.UserNotSignedUp));

        if (!user.isEmailConfirmed()) {
            throw new InvalidRequestException("Please confirm your email before logging in",
                    InvalidRequestExceptionType.EmailNotConfirmed);
        }

        String password = loginData.getPassword();

        if (!this.passwordService.isValidPassword(password, saltedPasswordHash)) {
            throw new InvalidRequestException("Invalid password",
                    InvalidRequestExceptionType.InvalidPassword);
        }

        SessionToken token = this.getNewSessionToken(user);

        SessionDataOutgoing sessionData = new SessionDataOutgoing(token.getId(),
                token.getCsrfToken(), user);

        this.sessionTokenRepository.save(token);

        return sessionData;
    }

    private SessionToken getNewSessionToken(User user) {
        String sessionToken = UUID.randomUUID().toString();
        String csrfToken = UUID.randomUUID().toString();

        ZonedDateTime expirationTimestamp = ZonedDateTime.now()
                .plusDays(this.sessionTokenValidityDays);

        return new SessionToken(sessionToken, user, expirationTimestamp, csrfToken);
    }

    public Long getAuthenticatedUserId(SessionDataIncoming sessionData) {
        SessionToken token = this.sessionTokenRepository.findById(sessionData.getSessionToken())
                .orElseThrow(() -> new InvalidRequestException("Invalid session token"));

        if (token.hasExpired()) {
            throw new InvalidRequestException("Expired token");
        }

        sessionData.checkValidityAgainstToken(token);

        return token.getUserId();
    }

}
