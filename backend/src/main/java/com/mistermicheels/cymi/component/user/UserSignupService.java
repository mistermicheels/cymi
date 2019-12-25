package com.mistermicheels.cymi.component.user;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.common.error.InvalidRequestExceptionType;
import com.mistermicheels.cymi.config.security.SecurityProperties;
import com.mistermicheels.cymi.io.email.EmailService;
import com.mistermicheels.cymi.io.email.emailMessage.ConfirmEmailEmailMessage;
import com.mistermicheels.cymi.io.email.emailMessage.InvitationEmailMessage;

@Service
class UserSignupService {

    private final UserRepository repository;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final PasswordService passwordService;
    private final EmailService emailService;

    private final int emailConfirmationTokenValidityDays;

    @Autowired
    UserSignupService(UserRepository repository,
            EmailConfirmationTokenRepository emailConfirmationTokenRepository,
            PasswordService passwordService, EmailService emailService,
            SecurityProperties securityProperties) {
        this.repository = repository;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.passwordService = passwordService;
        this.emailService = emailService;

        this.emailConfirmationTokenValidityDays = securityProperties
                .getEmailConfirmationTokenValidityDays();
    }

    public void signUpUser(LoginData loginData, String defaultDisplayName) {
        this.signUpUser(loginData, defaultDisplayName, null);
    }

    @Transactional
    public void signUpUser(LoginData loginData, String defaultDisplayName,
            String emailConfirmationToken) {
        String emailLowerCase = loginData.getEmailLowerCase();

        // (unlikely) race condition will be caught by unique index
        Optional<User> existingWithEmail = this.repository.findByEmail(emailLowerCase);
        User user = existingWithEmail.orElseGet(() -> new User(emailLowerCase));

        this.checkUserNotSignedUpYet(user);

        String password = loginData.getPassword();
        String saltedPasswordHash = this.passwordService.getSaltedPasswordHash(password);
        user.signup(saltedPasswordHash, defaultDisplayName);

        this.markEmailConfirmedIfTokenValid(user, emailConfirmationToken);

        if (user.isEmailConfirmed()) {
            this.repository.save(user);
        } else {
            // save and flush so the user gets its DB-generated ID
            this.repository.saveAndFlush(user);

            EmailConfirmationToken newConfirmationToken = this.getNewEmailConfirmationToken(user);
            this.emailConfirmationTokenRepository.save(newConfirmationToken);

            ConfirmEmailEmailMessage emailMessage = new ConfirmEmailEmailMessage(emailLowerCase,
                    newConfirmationToken.getId());

            this.emailService.send(emailMessage);
        }
    }

    private void checkUserNotSignedUpYet(User user) {
        if (user.getSaltedPasswordHash().isPresent()) {
            throw new InvalidRequestException(
                    "There is already a signed up user for this email address",
                    InvalidRequestExceptionType.EmailAlreadyTaken);
        }
    }

    private void markEmailConfirmedIfTokenValid(User user, String emailConfirmationToken) {
        if (emailConfirmationToken != null) {
            Optional<EmailConfirmationToken> token = this.emailConfirmationTokenRepository
                    .findById(emailConfirmationToken);

            if (token.isPresent() && token.get().isValidForUserId(user.getId())) {
                user.confirmEmail();
            }
        }
    }

    private EmailConfirmationToken getNewEmailConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();

        ZonedDateTime expirationTimestamp = ZonedDateTime.now()
                .plusDays(this.emailConfirmationTokenValidityDays);

        return new EmailConfirmationToken(token, user, expirationTimestamp);
    }

    @Transactional
    public User inviteUser(String email, String invitingGroupName) {
        String emailLowerCase = email.toLowerCase();
        this.checkEmailUnique(emailLowerCase);

        User user = new User(emailLowerCase);

        // save and flush so the user gets its DB-generated ID
        this.repository.saveAndFlush(user);

        EmailConfirmationToken emailConfirmationToken = this.getNewEmailConfirmationToken(user);
        this.emailConfirmationTokenRepository.save(emailConfirmationToken);

        InvitationEmailMessage emailMessage = new InvitationEmailMessage(emailLowerCase,
                invitingGroupName, emailConfirmationToken.getId());

        this.emailService.send(emailMessage);

        return user;
    }

    private void checkEmailUnique(String emailLowerCase) {
        // ignores possible (but unlikely) race conditions
        // those will be caught by the unique index

        Optional<User> existingWithEmail = this.repository.findByEmail(emailLowerCase);

        if (existingWithEmail.isPresent()) {
            throw new InvalidRequestException("There is already a user with the same email address",
                    InvalidRequestExceptionType.EmailAlreadyTaken);
        }
    }

    @Transactional
    public void confirmEmail(String emailConfirmationToken) {
        String invalidTokenMessage = "Invalid email confirmation token";

        EmailConfirmationToken token = this.emailConfirmationTokenRepository
                .findByIdWithUser(emailConfirmationToken)
                .orElseThrow(() -> new InvalidRequestException(invalidTokenMessage));

        if (token.hasExpired()) {
            throw new InvalidRequestException(invalidTokenMessage);
        }

        User userFromToken = token.getUser();
        userFromToken.confirmEmail();
        this.repository.save(userFromToken);
        this.emailConfirmationTokenRepository.delete(token);
    }

}
